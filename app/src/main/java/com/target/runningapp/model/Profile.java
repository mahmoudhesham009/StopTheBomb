package com.target.runningapp.model;

public class Profile {
    private int kM;
    private int cal;
    private int missions;
    private int level;
    private int xp;


    public Profile(){}

    public Profile(int level, int xp) {
        this.level = level;
        this.xp = xp;
    }

    public int getkM() {
        return kM;
    }

    public void setkM(int kM) {
        this.kM = kM;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public int getMissions() {
        return missions;
    }

    public void setMissions(int missions) {
        this.missions = missions;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
