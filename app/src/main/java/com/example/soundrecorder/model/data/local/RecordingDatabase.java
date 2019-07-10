package com.example.soundrecorder.model.data.local;

import android.content.Context;

import com.example.soundrecorder.model.Recording;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recording.class}, version = 2, exportSchema = false)
public abstract class RecordingDatabase extends RoomDatabase {

    public static RecordingDatabase instance;

    public abstract RecordingDao recordingDao();

    public static synchronized RecordingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecordingDatabase.class, "recording_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
