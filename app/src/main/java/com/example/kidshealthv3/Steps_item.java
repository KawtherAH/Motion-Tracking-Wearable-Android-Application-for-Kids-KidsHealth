package com.example.kidshealthv3;

public class Steps_item {
    private String date;
    private String data;
    private long timestamp;
    int steps;

    public Steps_item(String date, String steps, long timestamp) {
        this.date = date;
        this.data = steps;
        this.timestamp = timestamp;
    }

    public Steps_item() {

    }

    public Steps_item(String s, int nSteps) {
        this.date=s;
        this.steps=nSteps;
    }

    public String getDate() {
        return date;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }


}