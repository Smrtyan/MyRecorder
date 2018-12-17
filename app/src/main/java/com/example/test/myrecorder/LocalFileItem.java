package com.example.test.myrecorder;

public class LocalFileItem {
    private String displayName;
    private String durationSeconds;
    private String recordedDate;
    private String savedName;

    private int isUploaded;
    LocalFileItem(String savedName,String displayName, String durationSeconds, String recordedDate,int isUploaded){
        this.displayName = displayName;
        this.durationSeconds = durationSeconds;
        this.recordedDate = recordedDate;
        this.savedName=savedName;
        this.isUploaded = isUploaded;
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
    int getIsUploaded(){return  isUploaded; }

    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals( Object obj) {
        return this.equals(((LocalFileItem)obj));
    }
}
