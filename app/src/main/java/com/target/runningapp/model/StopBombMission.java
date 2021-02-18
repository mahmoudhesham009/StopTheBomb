package com.target.runningapp.model;

public class StopBombMission {
    long time;
    int numOfMissions;
    int missionUpperRange;
    int missionLowerRange;



    public StopBombMission(long time, int numOfMissions, int missionUpperRange, int missionLowerRange) {
        this.time = time;
        this.numOfMissions = numOfMissions;
        this.missionUpperRange = missionUpperRange;
        this.missionLowerRange = missionLowerRange;
    }

    public long getTime() {
        return time;
    }

    public int getNumOfMissions() {
        return numOfMissions;
    }

    public int getMissionUpperRange() {
        return missionUpperRange;
    }

    public int getMissionLowerRange() {
        return missionLowerRange;
    }
}
