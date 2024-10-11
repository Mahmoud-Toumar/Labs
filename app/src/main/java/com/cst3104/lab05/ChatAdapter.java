package com.cst3104.lab05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private ArrayList<Message> messages;
    private LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_message, parent, false);
        }

        TextView sendTextView = convertView.findViewById(R.id.sendTextView);
        TextView receiveTextView = convertView.findViewById(R.id.receiveTextView);
        Message message = messages.get(position);

        if (message.isSent()) {
            sendTextView.setText(message.getText());
            sendTextView.setVisibility(View.VISIBLE);
            receiveTextView.setVisibility(View.GONE);
        } else {
            receiveTextView.setText(message.getText());
            receiveTextView.setVisibility(View.VISIBLE);
            sendTextView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
