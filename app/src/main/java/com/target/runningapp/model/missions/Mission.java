package com.target.runningapp.model.missions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public abstract class Mission {
    public abstract ArrayList<MarkerOptions> setMission(ArrayList<LatLng> latLngs, LatLng myLoc);
}
