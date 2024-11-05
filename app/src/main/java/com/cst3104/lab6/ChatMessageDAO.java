package com.cst3104.lab6;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {
    // Method to insert a message
    @Insert
    long insertMessage(ChatMessage message);

    // Method to retrieve all messages
    @Query("SELECT * FROM ChatMessage")
    List<ChatMessage> getAllMessages();

    // Method to delete a specific message
    @Delete
    void deleteMessage(ChatMessage message);
}
