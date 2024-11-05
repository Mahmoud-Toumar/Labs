package com.cst3104.lab6;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private MessageDatabase db;
    private ChatMessageDAO chatMessageDAO;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<ChatMessage> messages;

    private final String[] messageSequence = {"a", "b", "c", "d", "e"};
    private int currentMessageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        Button sendButton = findViewById(R.id.sendButton);
        Button receiveButton = findViewById(R.id.receiveButton);

        db = Room.databaseBuilder(getApplicationContext(),
                        MessageDatabase.class, "chat_message_database")
                .build();
        chatMessageDAO = db.chatMessageDAO();

        messages = new ArrayList<>(); // Initialize the list here
        messageAdapter = new MessageAdapter(messages, this::deleteMessage);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMessages();

        sendButton.setOnClickListener(v -> addMessage(getString(R.string.sent_message)));
        receiveButton.setOnClickListener(v -> addMessage(getString(R.string.received_message)));
    }

    private void loadMessages() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (messages == null) { // Null check to avoid NullPointerException
                messages = new ArrayList<>();
            }
            messages.clear();
            messages.addAll(chatMessageDAO.getAllMessages());
            runOnUiThread(() -> messageAdapter.notifyDataSetChanged());
        });
    }

    private void addMessage(String type) {
        String messageText = messageSequence[currentMessageIndex];
        long timestamp = System.currentTimeMillis();
        ChatMessage message = new ChatMessage(type + ": " + messageText, timestamp);

        currentMessageIndex = (currentMessageIndex + 1) % messageSequence.length;

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            chatMessageDAO.insertMessage(message);
            runOnUiThread(() -> {
                messages.add(message);
                messageAdapter.notifyItemInserted(messages.size() - 1);
                messageRecyclerView.scrollToPosition(messages.size() - 1);
            });
        });
    }

    private void deleteMessage(ChatMessage message) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Message")
                .setMessage("Do you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        chatMessageDAO.deleteMessage(message);
                        runOnUiThread(() -> {
                            int position = messages.indexOf(message);
                            if (position != -1) {
                                messages.remove(position);
                                messageAdapter.notifyItemRemoved(position);

                                Snackbar.make(findViewById(R.id.main), "Message deleted", Snackbar.LENGTH_LONG)
                                        .setAction("Undo", v -> undoDelete(message, position))
                                        .show();
                            }
                        });
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void undoDelete(ChatMessage message, int position) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            chatMessageDAO.insertMessage(message);
            runOnUiThread(() -> {
                messages.add(position, message);
                messageAdapter.notifyItemInserted(position);
                messageRecyclerView.scrollToPosition(position);
            });
        });
    }
}
