package com.example.soundrecorder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recording_table")
public class Recording {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String filepath;
    private String length;
    private String date;

    public Recording(String name, String filepath, String length, String date) {
        this.name = name;
        this.filepath = filepath;
        this.length = length;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
