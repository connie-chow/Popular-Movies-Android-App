package com.example.project2_movies;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieRoom.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile AppDatabase INSTANCE;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movies";
    private static final Object LOCK = new Object();

    public static AppDatabase getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if(INSTANCE == null)
                {
                    Log.d(LOG_TAG, "Creating new database instance");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "movies")
                            .fallbackToDestructiveMigration()
                            .build();
                    // .allowMainThreadQueries().build();
                    // Queries should be done on separate thread, we allow this temporarily to
                    // check that the database is working
                }
                Log.d(LOG_TAG, "Getting the database instance");
            }
        }

        return INSTANCE;
    }


}
