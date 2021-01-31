package com.target.runningapp.model;

import android.graphics.Bitmap;

public class HistoryMission {
    int Time;
    float distance;
    boolean win;

    public HistoryMission(int time, float distance, boolean win) {
        Time = time;
        this.distance = distance;
        this.win = win;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
