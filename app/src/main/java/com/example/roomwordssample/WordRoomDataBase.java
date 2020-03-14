package com.example.roomwordssample;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDataBase extends RoomDatabase {

    public abstract WordDao wordDao();

    /* Here the code to create database */
    private static WordRoomDataBase INSTANCE; //Instance - экземпляр

    public static WordRoomDataBase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (WordRoomDataBase.class) {
                if(INSTANCE == null) {
                    //Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDataBase.class, "word_database")
                            //Wipes and rebuilds instead of migrating
                            //if no Migration object.
                            //Migration is not part of this practical
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
