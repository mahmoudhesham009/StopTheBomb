package com.target.runningapp.model.missions;

import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class StopBombMission {
    int time;
    int numOfMissions;
    int level;
    int missionUpperRange;
    int missionLowerRanget;


    public StopBombMission(int level) {
        this.level=level;
        this.time = 15;
        this.numOfMissions = 2;
        this.missionUpperRange=600;
        this.missionLowerRanget=500;
    }

    public int getTime() {
        return time;
    }

    public int getNumOfMissions() {
        return numOfMissions;
    }

    public int getMissionUpperRange() {
        return missionUpperRange;
    }

    public int getMissionLowerRanget() {
        return missionLowerRanget;
    }
}
