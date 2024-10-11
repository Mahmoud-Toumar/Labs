package com.cst3104.lab05;

public class Message {
    private String text;
    private boolean isSent;

    public Message(String text, boolean isSent) {
        this.text = text;
        this.isSent = isSent;
    }

    public String getText() {
        return text;
    }

    public boolean isSent() {
        return isSent;
    }
}
