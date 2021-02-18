package com.target.runningapp.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.target.runningapp.R;

import java.util.ArrayList;
import java.util.List;

public class RunService extends Service {
    FusedLocationProviderClient mLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    MutableLiveData<ArrayList<Location>> mTrackLiveData=new MutableLiveData<>();;
    MutableLiveData<Long> mTimerMutableLiveData= new MutableLiveData<>();
    ArrayList<Location>mTrack=new ArrayList<>();
    CountDownTimer countDownTimer;
    boolean inMission=false;

    IBinder myBinder=new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest=LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(inMission){
                    mTrack.add(locationResult.getLastLocation());
                    Log.d("vml in from service: ",mTrack.size()+"");
                    mTrackLiveData.postValue(mTrack);
                }

                else{
                    Log.d("vml out from service: ",locationResult.getLastLocation().getLatitude()+","+locationResult.getLastLocation().getLongitude());
                    mTrack.clear();
                    mTrack.add(locationResult.getLastLocation());
                    mTrackLiveData.postValue(mTrack);
                }
            }
        };
        checkPermissions();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification= new NotificationCompat.Builder(this,"myId")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Keep Running")
                .setContentText("you are doing a good job")
                .build();

        Log.d("vml single from service: ","hello");
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public RunService getService(){
            return RunService.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    public void activeClientProvider(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        }
        else {
            Toast.makeText(this,"Active Permission to access location",Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Location> getTrack() {
        return mTrack;
    }

    public LiveData<ArrayList<Location>> getTrackLiveData() {
        return mTrackLiveData;
    }

    public LiveData<Long> getTimerMutableLiveData() {
        return mTimerMutableLiveData;
    }

    public void startTimer(long i){
        countDownTimer=new CountDownTimer(i*1000*60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerMutableLiveData.postValue(millisUntilFinished);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void stopTimer(){
        countDownTimer.cancel();
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setInMission(boolean inMission) {
        this.inMission = inMission;
    }

    public void checkPermissions(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {activeClientProvider();}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }


}
