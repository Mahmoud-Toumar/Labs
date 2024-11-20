package com.cst3104.lab05;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Main activity class for managing chat messages.
 * Integrates Room database and options menu functionality.
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

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI components
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);
        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        messageListView.setAdapter(chatAdapter);

        // Initialize Room database
        messageDatabase = Room.databaseBuilder(
                getApplicationContext(),
                MessageDatabase.class,
                "chat_message_database"
        ).build();
        chatMessageDAO = messageDatabase.cmDAO();

        // Load initial messages from the database
        loadMessages();

        // Set up button listeners
        findViewById(R.id.sendButton).setOnClickListener(v -> addMessage(true));
        findViewById(R.id.receiveButton).setOnClickListener(v -> addMessage(false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_clear_list) {
            clearList();
            return true;
        } else if (itemId == R.id.action_load_list) {
            loadMessages();
            return true;
        } else if (itemId == R.id.action_delete_all) {
            deleteAllMessages();
            return true;
        } else if (itemId == R.id.action_help) {
            showHelpDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addMessage(boolean isSent) {
        String text = messageEditText.getText().toString();
        if (!text.isEmpty()) {
            Message newMessage = new Message(text, isSent);
            messages.add(newMessage);
            chatAdapter.notifyDataSetChanged();
            messageEditText.setText("");

            // Save to database in a background thread
            backgroundThread.execute(() -> chatMessageDAO.insertMessage(newMessage));
        }
    }

    private void clearList() {
        messages.clear();
        chatAdapter.notifyDataSetChanged();
        Snackbar.make(messageListView, getString(R.string.snackbar_cleared), Snackbar.LENGTH_SHORT).show();
    }

    private void loadMessages() {
        backgroundThread.execute(() -> {
            messages.clear();
            messages.addAll(chatMessageDAO.getAllMessages());
            runOnUiThread(() -> chatAdapter.notifyDataSetChanged());
        });
    }

    private void deleteAllMessages() {
        backgroundThread.execute(() -> {
            chatMessageDAO.deleteAllMessages();
            messages.clear();
            runOnUiThread(() -> {
                chatAdapter.notifyDataSetChanged();
                Snackbar.make(messageListView, getString(R.string.snackbar_deleted_all), Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.help_dialog_title))
                .setMessage(getString(R.string.help_dialog_message, "Your Name")) // Replace with your name
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
