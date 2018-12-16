package com.example.test.myrecorder;

public class LocalFileItem {
    private String displayName;
    private String durationSeconds;
    private String recordedDate;
    private String savedName;
    LocalFileItem(String savedName, String displayName, String durationSeconds, String recordedDate){
        this.displayName = displayName;
        this.durationSeconds = durationSeconds;
        this.recordedDate = recordedDate;
        this.savedName=savedName;
    }

    String getDisplayName(){
        return displayName;
    }

    String getDurationSeconds(){
        return durationSeconds;
    }

    String getRecordedDate(){ return recordedDate; }
    String getSavedName(){
        return savedName;
    }

    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals( Object obj) {
        return this.equals(((LocalFileItem)obj));
    }
}
