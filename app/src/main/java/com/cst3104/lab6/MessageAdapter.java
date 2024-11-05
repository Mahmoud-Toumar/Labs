package com.cst3104.lab6;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<ChatMessage> messages;
    private final OnMessageLongClickListener longClickListener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessage message);
    }

    public MessageAdapter(List<ChatMessage> messages, OnMessageLongClickListener longClickListener) {
        this.messages = messages;
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        String messageText = messages.get(position).getMessageText();
        return messageText.startsWith("Received") ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        }
        return new MessageViewHolder(view, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getMessageText());

        String timestamp = new SimpleDateFormat("EEE, dd-MMM-yyyy hh:mm:ss a", Locale.getDefault())
                .format(new Date(message.getTimestamp()));
        holder.timestampTextView.setText(timestamp);

        holder.itemView.setTag(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;

        public MessageViewHolder(@NonNull View itemView, OnMessageLongClickListener longClickListener) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);

            itemView.setOnLongClickListener(v -> {
                ChatMessage message = (ChatMessage) v.getTag();
                if (message != null) {
                    longClickListener.onMessageLongClick(message);
                }
                return true;
            });
        }
    }
}
