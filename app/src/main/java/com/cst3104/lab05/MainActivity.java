package com.cst3104.lab05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Main activity class for managing chat messages.
 * Integrates Room database for storing and retrieving messages.
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Message> messages;
    private ChatAdapter chatAdapter;
    private ListView messageListView;
    private EditText messageEditText;

    private MessageDatabase messageDatabase;
    private com.cst3104.lab05.ChatMessageDAO chatMessageDAO;

    private Executor backgroundThread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);

        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);
        Button receiveButton = findViewById(R.id.receiveButton);

        messageListView.setAdapter(chatAdapter);

        // Initialize the Room database
        messageDatabase = Room.databaseBuilder(
                getApplicationContext(),
                MessageDatabase.class,
                "chat_message_database"
        ).build();
        chatMessageDAO = messageDatabase.cmDAO();

        // Load existing messages from the database
        loadMessages();

        // Set up button click listeners
        sendButton.setOnClickListener(v -> addMessage(true));
        receiveButton.setOnClickListener(v -> addMessage(false));

        // Set up long click listener for deleting messages
        messageListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Message messageToDelete = messages.get(position);
            showDeleteDialog(position, messageToDelete);
            return true;
        });
    }

    /**
     * Loads all messages from the database into the messages list.
     */
    private void loadMessages() {
        backgroundThread.execute(() -> {
            messages.addAll(chatMessageDAO.getAllMessages());
            runOnUiThread(() -> chatAdapter.notifyDataSetChanged());
        });
    }

    /**
     * Adds a new message to the messages list and database.
     *
     * @param isSent True if the message is sent, false if received.
     */
    private void addMessage(boolean isSent) {
        String text = messageEditText.getText().toString();
        if (!text.isEmpty()) {
            Message newMessage = new Message(text, isSent);
            messages.add(newMessage);
            chatAdapter.notifyDataSetChanged();
            messageEditText.setText("");

            // Save the message to the database
            backgroundThread.execute(() -> chatMessageDAO.insertMessage(newMessage));
        }
    }

    /**
     * Shows a confirmation dialog to delete a message.
     *
     * @param position The position of the message in the list.
     * @param message  The message to delete.
     */
    private void showDeleteDialog(int position, Message message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message, position))
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> deleteMessage(position, message))
                .setNegativeButton(R.string.dialog_negative, null)
                .show();
    }

    /**
     * Deletes a message from the list and database, with Snackbar Undo functionality.
     *
     * @param position The position of the message in the list.
     * @param message  The message to delete.
     */
    private void deleteMessage(int position, Message message) {
        Message removedMessage = messages.remove(position);
        chatAdapter.notifyDataSetChanged();

        // Delete the message from the database
        backgroundThread.execute(() -> chatMessageDAO.deleteMessage(message));

        // Show Snackbar with Undo option
        Snackbar.make(messageListView, getString(R.string.snackbar_message, position), Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action, v -> undoDelete(position, removedMessage))
                .show();
    }

    /**
     * Restores a deleted message back to the list and database.
     *
     * @param position The position to restore the message to.
     * @param message  The message to restore.
     */
    private void undoDelete(int position, Message message) {
        messages.add(position, message);
        chatAdapter.notifyDataSetChanged();

        // Re-insert the message into the database
        backgroundThread.execute(() -> chatMessageDAO.insertMessage(message));
    }
}
