package com.example.soundrecorder.model.data;

import android.app.Application;
import android.os.AsyncTask;

import com.example.soundrecorder.model.Recording;
import com.example.soundrecorder.model.data.local.RecordingDao;
import com.example.soundrecorder.model.data.local.RecordingDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecordingRepository {

    private RecordingDao recordingDao;
    private LiveData<List<Recording>> allRecordings;

    public RecordingRepository(Application application) {
        RecordingDatabase database = RecordingDatabase.getInstance(application);
        recordingDao = database.recordingDao();
        allRecordings = recordingDao.getAllRecordings();
    }

    public LiveData<List<Recording>> getAllRecordings() {
        return allRecordings;
    }

    public void insert(Recording recording) {
        new InsertRecordingAsyncTask(recordingDao).execute(recording);
    }

    public void delete(Recording recording) {
        new DeleteRecordingAsyncTask(recordingDao).execute(recording);
    }

    public void deleteAllRecordings() {
        new DeleteAllRecordingsAsyncTask(recordingDao).execute();
    }

    private static class InsertRecordingAsyncTask extends AsyncTask<Recording, Void, Void> {
        private RecordingDao recordingDao;

        private InsertRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }

        @Override
        protected Void doInBackground(Recording... recordings) {
            recordingDao.insert(recordings[0]);
            return null;
        }
    }

    private static class DeleteRecordingAsyncTask extends AsyncTask<Recording, Void, Void> {
        private RecordingDao recordingDao;

        private DeleteRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }

        @Override
        protected Void doInBackground(Recording... recordings) {
            recordingDao.delete(recordings[0]);
            return null;
        }
    }

    private static class DeleteAllRecordingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private RecordingDao recordingDao;

        private DeleteAllRecordingsAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recordingDao.deleteAllRecordings();
            return null;
        }
    }
}
