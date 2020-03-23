package com.example.roomwordssample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //Add the onOpen() callback in the WordRoomDatabase class
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PpopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final  WordDao mDao;
        String[] words = {"dolphin", "crocodile", "cobra", "monkey",
        "elephant", "bird\"Dodo\"", "mouse"};

        PpopulateDbAsync(WordRoomDataBase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Start the app with clean database every time.
            //Not needed if you only populate the database
            //when it first created
            mDao.deleteAllWords();

            for (int i = 0; i< words.length - 1; i++) {
                Word word = new Word(words[i]);
                mDao.insert(word);
            }

            return null;
        }
    }
}
