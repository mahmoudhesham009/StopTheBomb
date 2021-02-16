package com.target.runningapp.model;

public class StopBombMission {
    long time;
    int numOfMissions;
    int level;
    int missionUpperRange;
    int missionLowerRanget;



    public StopBombMission(long time, int numOfMissions, int missionUpperRange, int missionLowerRanget) {
        this.time = time;
        this.numOfMissions = numOfMissions;
        this.missionUpperRange = missionUpperRange;
        this.missionLowerRanget = missionLowerRanget;
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

    public int getMissionLowerRanget() {
        return missionLowerRanget;
    }
}
