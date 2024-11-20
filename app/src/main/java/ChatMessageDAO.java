package com.cst3104.lab05;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) for interacting with the ChatMessage table in the database.
 */
@Dao
public interface ChatMessageDAO {

    /**
     * Inserts a new ChatMessage into the database.
     *
     * @param message The ChatMessage to insert.
     * @return The newly generated ID of the inserted message.
     */
    @Insert
    long insertMessage(Message message);

    /**
     * Retrieves all ChatMessages from the database.
     *
     * @return A list of all stored ChatMessages.
     */
    @Query("SELECT * FROM ChatMessage")
    List<Message> getAllMessages();

    /**
     * Deletes a specific ChatMessage from the database.
     *
     * @param message The ChatMessage to delete.
     */
    @Delete
    void deleteMessage(Message message);
}
