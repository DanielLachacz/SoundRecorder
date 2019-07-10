package com.example.soundrecorder.model.data.local;

import com.example.soundrecorder.model.Recording;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RecordingDao {

    @Insert
    void insert(Recording recording);

    @Delete
    void delete(Recording recording);

    @Query("DELETE FROM recording_table")
    void deleteAllRecordings();

    @Query("SELECT * FROM recording_table")
    LiveData<List<Recording>> getAllRecordings();
}
