package com.example.soundrecorder.viewmodel;

import android.app.Application;

import com.example.soundrecorder.model.Recording;
import com.example.soundrecorder.model.data.RecordingRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ListViewModel extends AndroidViewModel {

    private RecordingRepository recordingRepository;
    private LiveData<List<Recording>> allRecordings;

    public ListViewModel(@NonNull Application application) {
        super(application);
        recordingRepository = new RecordingRepository(application);
        allRecordings = recordingRepository.getAllRecordings();
    }

    public LiveData<List<Recording>> getAllRecordings() {
        return allRecordings;
    }

    public void deleteRecording(Recording recording) {
        recordingRepository.delete(recording);
    }

    public void deleteAllRecordings() {
        recordingRepository.deleteAllRecordings();
    }
}
