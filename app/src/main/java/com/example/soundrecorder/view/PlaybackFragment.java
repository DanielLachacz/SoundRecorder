package com.example.soundrecorder.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundrecorder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlaybackFragment extends DialogFragment {

    private SeekBar seekBar;
    private FloatingActionButton playbackButton;
    private TextView reocordingTextView;
    private TextView progressTextView;
    private Boolean playing = false;
    private MediaPlayer mediaPlayer = null;
    private String name;
    private String filePath;
    private String length;
    private static final String TAG = "PlaybackFragment";
    private static final String TAG2 = "PlaybackFragment playFromPoint";
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playback_fragment, container, false);

        seekBar = v.findViewById(R.id.seek_bar);
        playbackButton = v.findViewById(R.id.playback_button);
        reocordingTextView = v.findViewById(R.id.recording_text_view);
        progressTextView = v.findViewById(R.id.progress_text_view);

        playbackButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        Bundle bundle = getArguments();
        name = bundle.getString("name", "");
        filePath = bundle.getString("filePath", "");
        length = bundle.getString("length", "");

        reocordingTextView.setText(name);

        playbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (!playing) {

                    if (mediaPlayer == null) {
                        play();
                    } else {
                        resume();
                    }
                } else {
                    pause();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    handler.removeCallbacks(runnable);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    progressTextView.setText(String.format("%02d:%02d", minutes,seconds));

                    updateSeekBar();
                }
                else if (mediaPlayer == null && fromUser) {
                    playFromPoint(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    handler.removeCallbacks(runnable);
                    mediaPlayer.seekTo(seekBar.getProgress());

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    progressTextView.setText(String.format("%02d:%02d", minutes,seconds));
                    updateSeekBar();
                }
            }
        });

        return v;
    }

    private void play() {

        playing = true;

        playbackButton.setImageResource(R.drawable.ic_pause_black_true_24dp);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(getContext(), "Playing...", Toast.LENGTH_SHORT).show();

            seekBar.setMax(mediaPlayer.getDuration());
        } catch (Exception e) {
            Log.e(TAG, "prepare() failed");
        }

        updateSeekBar();
    }

    private void playFromPoint(int progress) {

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(progress);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    stop();
                }
            });
        } catch (IOException e) {
            Log.e(TAG2, "prepare() failed");
        }

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void pause() {

        playing = false;

        playbackButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        mediaPlayer.pause();
    }

    private void resume() {

        playing = true;

        playbackButton.setImageResource(R.drawable.ic_pause_black_true_24dp);

        mediaPlayer.start();
    }

    private void stop() {

        playbackButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        handler.removeCallbacks(runnable);

        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(currentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(currentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                progressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            stop();
        }
    }
}
