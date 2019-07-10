package com.example.soundrecorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.soundrecorder.R;
import com.example.soundrecorder.model.Recording;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecordingViewHolder> {

    private List<Recording> recordings = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recording_item, parent, false);
        return new RecordingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingViewHolder holder, int position) {
        Recording currentRecording = recordings.get(position);
        holder.nameTextView.setText(String.valueOf(currentRecording.getName()));
        holder.dateTextView.setText(String.valueOf(currentRecording.getDate()));
        holder.lengthTextView.setText(currentRecording.getLength());
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
        notifyDataSetChanged();
    }

    public Recording getRecordingAt(int position) {
        return recordings.get(position);
    }

    public class RecordingViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView lengthTextView;

        public RecordingViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            lengthTextView = itemView.findViewById(R.id.length_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(recordings.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Recording recording);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
