package com.example.test.myrecorder;

public class MyListViewItem {
    private String displayName;
    private String durationSeconds;
    private String recordedDate;
    MyListViewItem(String displayName, String durationSeconds, String recordedDate){
        this.displayName = displayName;
        this.durationSeconds = durationSeconds;
        this.recordedDate = recordedDate;
    }

    String getDisplayName(){
        return displayName;
    }

    String getDurationSeconds(){
        return durationSeconds;
    }

    String getRecordedDate(){
        return recordedDate;
    }

    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals( Object obj) {
        return this.equals(((MyListViewItem)obj));
    }
}
