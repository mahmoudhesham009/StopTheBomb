package com.target.runningapp.model.missions;

public class StopBombMission {
    int time;
    int numOfMissions;
    int level;
    int missionUpperRange;
    int missionLowerRanget;


    public StopBombMission(int level) {
        this.level = level;
        this.time = 15;
        this.numOfMissions = 2;
        this.missionUpperRange = 600;
        this.missionLowerRanget = 500;
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
