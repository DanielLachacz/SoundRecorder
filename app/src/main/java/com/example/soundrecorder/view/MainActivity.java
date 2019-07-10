package com.example.soundrecorder.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.soundrecorder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton listButton, recordButton, stopButton;
    private MediaRecorder mediaRecorder;
    private String filePath;
    private String chronoTime;
    private Chronometer chronometer;
    private long pauseOffset;
    private final int REQUEST_PERMISSION_CODE = 1000;
    private boolean startRecording = false;
    private Boolean playing = false;
    private String time;
    long pausedTime = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listButton = findViewById(R.id.list_button);
        recordButton = findViewById(R.id.record_button);
        stopButton = findViewById(R.id.stop_button);
        chronometer = findViewById(R.id.chronometer);

        listButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        recordButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        stopButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        stopButton.setEnabled(false);
        stopButton.setClickable(false);

        if (!checkPermission())
            requestPermission();

            recordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkPermission()) {

                            if (!playing) {

                                if (mediaRecorder == null) {
                                    onRecordButton();
                                } else {
                                   onResumeButton();
                                }
                            } else {
                               onPauseButton();
                            }
                    }
                    else
                    {
                        requestPermission();
                    }
                }

            } );

            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStopButton();
                }
            });

            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            });



    }

    private void onRecordButton() {

            playing = true;
            startRecording = true;

            recordButton.setImageResource(R.drawable.ic_pause_black_24dp);
            Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();

            listButton.setClickable(false);
            listButton.setEnabled(false);
            stopButton.setEnabled(true);
            listButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            stopButton.setClickable(true);
            stopButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();

            setupMediaRecorder();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void onPauseButton() {

        playing = false;
        startRecording = false;

        listButton.setClickable(false);
        listButton.setEnabled(false);
        listButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        recordButton.setImageResource(R.drawable.ic_mic_black_24dp);
        Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();

        pausedTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        //pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

        mediaRecorder.pause();

    }

    private void onResumeButton() {

        playing = true;

        recordButton.setImageResource(R.drawable.ic_pause_black_24dp);
        Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_SHORT).show();

        chronometer.setBase(SystemClock.elapsedRealtime() + pausedTime);
        chronometer.start();

        mediaRecorder.resume();
    }

    private void onStopButton() {

        startRecording = false;

        recordButton.setImageResource(R.drawable.ic_mic_black_24dp);

        listButton.setClickable(true);
        listButton.setEnabled(true);
        listButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        stopButton.setClickable(false);
        stopButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        Toast.makeText(getApplicationContext(), "Recording completed", Toast.LENGTH_SHORT).show();

        chronometer.stop();
        chronoTime = chronometer.getText().toString();
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        openSaveFragment();
    }

    public void setupMediaRecorder() {
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "recordings" + File.separator + getDateAndTime() + ".3gpp";
        File file = new File(filePath);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(file);
    }

    private String getDateAndTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        time = timeFormat.format(Calendar.getInstance().getTime());
        return  date + "-" + time;
    }

    private void openSaveFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("length", chronoTime);

        SaveFragment fragment = new SaveFragment();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "SaveFragment");
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;

        }
    }

        private boolean checkPermission() {
            int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int record_audio_resut = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                    record_audio_resut == PackageManager.PERMISSION_GRANTED;
        }

    @Override
    protected void onDestroy() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        super.onDestroy();
    }
}
