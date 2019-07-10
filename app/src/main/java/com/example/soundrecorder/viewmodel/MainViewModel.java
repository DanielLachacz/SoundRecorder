package com.example.soundrecorder.viewmodel;

import android.app.Application;

import com.example.soundrecorder.model.Recording;
import com.example.soundrecorder.model.data.RecordingRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel {

    private RecordingRepository recordingRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        recordingRepository = new RecordingRepository(application);
    }

    public void insertRecording(Recording recording) {
        recordingRepository.insert(recording);
    }

}
