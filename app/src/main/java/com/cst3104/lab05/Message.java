package com.cst3104.lab05;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a ChatMessage.
 * This class is used with the Room database to store messages.
 */
@Entity(tableName = "ChatMessage")
public class Message {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "is_sent")
    private boolean isSent;

    /**
     * Constructs a new Message object.
     *
     * @param text    The content of the message.
     * @param isSent  True if the message is sent, false if received.
     */
    public Message(String text, boolean isSent) {
        this.text = text;
        this.isSent = isSent;
    }

    // Getters and Setters

    /**
     * Gets the text of the message.
     *
     * @return The message content.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the message.
     *
     * @param text The new content of the message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns true if the message was sent, false otherwise.
     *
     * @return True if sent, false if received.
     */
    public boolean isSent() {
        return isSent;
    }

    /**
     * Sets the sent status of the message.
     *
     * @param sent True if sent, false if received.
     */
    public void setSent(boolean sent) {
        isSent = sent;
    }
}
