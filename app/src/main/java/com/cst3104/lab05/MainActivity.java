package com.cst3104.lab05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Message> messages;
    private ChatAdapter chatAdapter;
    private ListView messageListView;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);

        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);
        Button receiveButton = findViewById(R.id.receiveButton);

        messageListView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> addMessage(true));
        receiveButton.setOnClickListener(v -> addMessage(false));

        messageListView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        messages.remove(position);
                        chatAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void addMessage(boolean isSent) {
        String text = messageEditText.getText().toString();
        if (!text.isEmpty()) {
            messages.add(new Message(text, isSent));
            chatAdapter.notifyDataSetChanged();
            messageEditText.setText("");
        }
    }
}
