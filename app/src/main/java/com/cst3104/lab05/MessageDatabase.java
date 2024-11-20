package com.cst3104.lab05;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Abstract class for the Room database.
 * This class provides a connection to the database and the DAO for interacting with it.
 */
@Database(entities = {Message.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    /**
     * Provides access to the ChatMessageDAO for database operations.
     *
     * @return The DAO instance for ChatMessage operations.
     */
    public abstract com.cst3104.lab05.ChatMessageDAO cmDAO();
}
