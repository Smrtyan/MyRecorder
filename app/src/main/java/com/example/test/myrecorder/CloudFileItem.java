package com.example.test.myrecorder;

public class CloudFileItem {
    private String displayName;
    private String durationSeconds;
    private String recordedDate;
    Boolean isDownloaded;
    CloudFileItem(String displayName, String durationSeconds, String recordedDate,Boolean isDownloaded){
        this.displayName = displayName;
        this.durationSeconds = durationSeconds;
        this.recordedDate = recordedDate;
        this.isDownloaded = isDownloaded;
    }

    String getDisplayName(){
        return displayName;
    }

    String getDurationSeconds(){
        return durationSeconds;
    }

    String getRecordedDate(){ return recordedDate; }

    Boolean getIsDownloaded(){ return isDownloaded; }

    public String toString() {
        return displayName;
    }


    @Override
    public boolean equals( Object obj) {
        return this.equals(((CloudFileItem)obj));
    }
}
