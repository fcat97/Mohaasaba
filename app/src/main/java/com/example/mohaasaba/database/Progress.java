package com.example.mohaasaba.database;

public class Progress {
    public long commitDate;

    public int maxProgress;
    public int currentProgress;
    public int progressStep;
    public String progressUnit = "";

    public Progress(long commitDate) {
        this.commitDate = commitDate;
    }
}
