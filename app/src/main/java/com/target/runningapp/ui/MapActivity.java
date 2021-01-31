package com.target.runningapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.core.Repo;
import com.target.runningapp.ProfileActivity;
import com.target.runningapp.RunService;
import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.viewModel.MapViewModel;
import com.target.runningapp.R;
import com.target.runningapp.model.missions.StopBombMission;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//diff level and firebase
//mspeed & mDistance & mTimer

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Toolbar.OnMenuItemClickListener {
    private MapViewModel mViewModel;
    GoogleMap mGoogleMap;
    MapView mMapView;
    ImageView cameraToLocation;
    TextView mTimer;
    TextView mDistance;
    TextView mSpeed;
    Toolbar mToolbar;
    int seconds=0;

    LatLng locationLatLong;

    StopBombMission mMission = new StopBombMission( 1);


    ArrayList<Marker> mMarkerMissions = new ArrayList<>();
    ArrayList<Marker> mCheckedMissions = new ArrayList<>();

    ArrayList<MarkerOptions> pickedPoints=new ArrayList<>();
    ArrayList<Marker> mMarkerBombs = new ArrayList<>();
    ArrayList<Marker> mStoppedBombs = new ArrayList<>();

    Projection mProjection;
    Button startMission;
    Dialog dialog;
    LatLng oldLocation;

    float totalDistance=0;
    boolean missionSeted = false;
    boolean inMission = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        startMission = findViewById(R.id.start_mission_but);
        mMapView = findViewById(R.id.mapView);
        cameraToLocation = findViewById(R.id.cameraToLocation);
        mTimer = findViewById(R.id.timer);
        mDistance=findViewById(R.id.distance);
        mSpeed=findViewById(R.id.speed);
        mToolbar = findViewById(R.id.ToolBar);
        mToolbar.setOnMenuItemClickListener(this);

        cameraToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationLatLong != null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLong, 17f));
            }
        });


        startMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inMission = true;
                mGoogleMap.clear();
                findViewById(R.id.mission_starter).setVisibility(View.INVISIBLE);
                findViewById(R.id.statistics).setVisibility(View.VISIBLE);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLong, 15f));
                mProjection = mGoogleMap.getProjection();
                setBombs(mMission);
            }
        });

        mMapView.getMapAsync(this);
        mMapView.onCreate(null);
        mMapView.onResume();
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mViewModel.init(this);
        mViewModel.checkPermissions();

        mViewModel.getTimerLiveData().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                seconds++;
                mTimer.setText((aLong / 1000) / 60 + " : " + (aLong / 1000) % 60);
                mDistance.setText(String.format("%.2f",totalDistance/1000)+" Km");
                mSpeed.setText(String.format("%.2f",(totalDistance)/(seconds))+" m/sec");

                if (aLong < 1000) {
                    youLose(seconds,totalDistance);
                    reset();
                }

                else if (mStoppedBombs.size() >= mMission.getNumOfMissions()) {
                    youWin(seconds,totalDistance);
                    reset();
                }
            }
        });

        mViewModel.getLocationLiveData().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                locationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                if (!missionSeted && !inMission) {
                    setMissions(locationLatLong);
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLong, 17f));
                    missionSeted = true;
                }
                if (inMission) {
                    if(oldLocation!=null){
                        float[] results = new float[1];
                        Location.distanceBetween(oldLocation.latitude, oldLocation.longitude,
                                locationLatLong.latitude, locationLatLong.longitude, results);
                        totalDistance=totalDistance+results[0];
                        mGoogleMap.addPolyline(new PolylineOptions().add(locationLatLong,oldLocation).width(5).color(Color.rgb(85,136,163)));
                    }
                    oldLocation=locationLatLong;

                    isBombStopped(locationLatLong).subscribe(new io.reactivex.Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer i) {
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(mMarkerBombs.get(i).getPosition())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("myDameError", e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
                if (!inMission) {
                    mViewModel.checkCloseMission(locationLatLong).subscribe(new io.reactivex.Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Integer i) {
                            if (i == 123) {
                                findViewById(R.id.mission_starter).setVisibility(View.INVISIBLE);
                                for (int j = 0; j < mCheckedMissions.size(); j++)
                                    mCheckedMissions.get(j).remove();
                            } else {
                                mCheckedMissions.add(mGoogleMap.addMarker(new MarkerOptions()
                                        .position(mMarkerMissions.get(i).getPosition())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .zIndex(1)));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                }
            }
        });
    }


    private void setMissions(LatLng player) {
        ArrayList<MarkerOptions> missions = mViewModel.prepareMissions(player);
        mMarkerMissions.clear();
        for (MarkerOptions m : missions) {
            mMarkerMissions.add(mGoogleMap.addMarker(m));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_hjson)));
        mGoogleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getZIndex() == 1) {
            findViewById(R.id.mission_starter).setVisibility(View.VISIBLE);
        }
        return true;
    }


    public void setBombs(final StopBombMission mission) {
        mGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                getStreetPoints(bitmap).subscribe(new SingleObserver<ArrayList<LatLng>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ArrayList<LatLng> latLngs) {
                        ArrayList<MarkerOptions> markerOptions = prepareBombs(latLngs);
                        if(markerOptions.size()==0){
                            reset();
                            Toast.makeText(MapActivity.this, "Move to area with many public streets", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (MarkerOptions m : markerOptions) {
                                mMarkerBombs.add(mGoogleMap.addMarker(m));
                            }
                            mViewModel.startTimer(mMission.getTime());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
    }

    public Single<ArrayList<LatLng>> getStreetPoints(final Bitmap bitmap) {
        return Single.create(new SingleOnSubscribe<ArrayList<LatLng>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<LatLng>> e) throws Exception {
                ArrayList<LatLng> availableLatLng = new ArrayList<>();
                for (int i = 0; i < bitmap.getHeight(); i=i+10) {
                    for (int j = 0; j < bitmap.getWidth(); j=j+10) {
                        Color pixelColor = bitmap.getColor(j, i);
                        if (pixelColor.red() == pixelColor.green() && pixelColor.green() == pixelColor.blue() && pixelColor.blue() == 1) {
                            availableLatLng.add(mProjection.fromScreenLocation(new Point(j, i)));
                        }
                    }
                }
                e.onSuccess(availableLatLng);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Integer> isBombStopped(final LatLng myLoc) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                float[] results = new float[1];
                for (int i = 0; i < pickedPoints.size(); i++) {
                    Location.distanceBetween(pickedPoints.get(i).getPosition().latitude, pickedPoints.get(i).getPosition().longitude,
                            myLoc.latitude, myLoc.longitude, results);
                    if (results[0] < 10) {
                        if (!mStoppedBombs.contains(mMarkerBombs.get(i))) {
                            mStoppedBombs.add(mMarkerBombs.get(i));
                            e.onNext(i);
                        }

                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public ArrayList<MarkerOptions> prepareBombs(ArrayList<LatLng> latLngs) {
        int point;
        ArrayList<MarkerOptions> allPickedPoints=new ArrayList<>();
        float[] results = new float[3];
        for(int i=0; i<latLngs.size();i++){
            //point= new Random().nextInt(latLngs.size()-1);
            Location.distanceBetween(locationLatLong.latitude, locationLatLong.longitude,
                    latLngs.get(i).latitude, latLngs.get(i).longitude,
                    results);
            if(results[0]>mMission.getMissionLowerRanget()&&results[0]<mMission.getMissionUpperRange())
                allPickedPoints.add(new MarkerOptions()
                        .position(latLngs.get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        for(int i=0 ; i<mMission.getNumOfMissions();i++){
            if(allPickedPoints.size()<mMission.getNumOfMissions())break;
            pickedPoints.add(allPickedPoints.get(new Random().nextInt(allPickedPoints.size()-1)));
        }
        return pickedPoints;
    }


    public void youWin(int time, float distance) {
        //save km time xp at server
        mViewModel.submitMission(new HistoryMission(time, distance, true));
        showDialog("Mission Complete");
    }

    public void youLose(int time, float distance) {
        //save km time xp at server
        mViewModel.submitMission(new HistoryMission(time, distance, false));
        showDialog("Mission failed");
    }

    public void showDialog(String m) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.win_dialog);
        TextView message = dialog.findViewById(R.id.message);
        dialog.setCancelable(false);
        message.setText(m);
        dialog.findViewById(R.id.gotIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void reset(){
        mMarkerBombs.clear();
        mStoppedBombs.clear();
        pickedPoints.clear();
        mGoogleMap.clear();
        inMission = false;
        missionSeted = false;
        oldLocation=null;
        totalDistance=0;
        findViewById(R.id.statistics).setVisibility(View.INVISIBLE);
        mViewModel.stopTimer();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case R.id.logOut:
                mViewModel.signOut();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                break;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.mission_starter).getVisibility() == View.VISIBLE)
            findViewById(R.id.mission_starter).setVisibility(View.INVISIBLE);

        else if(inMission){
            final Dialog quit=new Dialog(this);
            quit.setContentView(R.layout.quit_dialog);
            quit.findViewById(R.id.Yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            quit.findViewById(R.id.No).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quit.dismiss();
                }
            });
            quit.show();
        }

        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        ContextCompat.startForegroundService(this,new Intent(this, RunService.class));
        super.onPause();

    }
}