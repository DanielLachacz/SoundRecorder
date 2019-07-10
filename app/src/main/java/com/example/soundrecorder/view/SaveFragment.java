package com.example.soundrecorder.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.soundrecorder.R;
import com.example.soundrecorder.model.Recording;
import com.example.soundrecorder.viewmodel.MainViewModel;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class SaveFragment extends DialogFragment {

    private Button saveButton;
    private Button cancelButton;
    private EditText nameEditText;
    private String name;
    private static final String TAG = "SaveFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.save_fragment, container, false);
        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        saveButton = v.findViewById(R.id.save_button);
        cancelButton = v.findViewById(R.id.cancel_button);
        nameEditText = v.findViewById(R.id.name_edit_text);

        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        nameEditText.setText(currentDate);

        Bundle bundle = getArguments();
        final String filePath = bundle.getString("filePath", "");
        final String length = bundle.getString("length", "");

        Log.d(TAG, "Bundles " + filePath + " / " + length);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = String.valueOf(nameEditText.getText());
                Recording recording = new Recording(name, filePath, length, currentDate);
                mainViewModel.insertRecording(recording);
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = String.valueOf(nameEditText.getText());
                Recording recording = new Recording(name, filePath, length, currentDate);
                mainViewModel.insertRecording(recording);
                getDialog().dismiss();
            }
        });

        return v;
    }

}
