package com.target.runningapp.viewModel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.repositories.AuthRepository;
import com.target.runningapp.repositories.HistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {
    Context mContext;
    FusedLocationProviderClient mLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    AuthRepository authRepository;
    HistoryRepository historyRepository;

    ArrayList<MarkerOptions> missions=new ArrayList<>();
    MutableLiveData<Location> mLocationMutableLiveData;
    MutableLiveData<Long> mTimerMutableLiveData;


    CountDownTimer countDownTimer;

    public void init(Context context){
        this.mContext=context;
        authRepository=AuthRepository.getInstance(context);
        historyRepository=HistoryRepository.getInstance();
        mLocationMutableLiveData=new MutableLiveData<>();
        mTimerMutableLiveData=new MutableLiveData<>();
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest=LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLocationMutableLiveData.postValue(locationResult.getLastLocation());
            }
        };
    }

    public LiveData<Location> getLocationLiveData(){
        return mLocationMutableLiveData;
    }
    public LiveData<Long> getTimerLiveData(){
        return mTimerMutableLiveData;
    }


    public Observable<Integer> checkCloseMission(final LatLng latLng){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                float[] results = new float[1];
                boolean noCloseMissions=true;
                for(int i=0 ;i<missions.size(); i++){
                    Location.distanceBetween(missions.get(i).getPosition().latitude, missions.get(i).getPosition().longitude,
                            latLng.latitude, latLng.longitude, results);
                    if(results[0]<20){
                        e.onNext(i);
                        noCloseMissions=false;
                    }
                }
                if(noCloseMissions) e.onNext(123);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Integer> checkCloseBomb(final LatLng latLng){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                float[] results = new float[1];
                boolean noCloseMissions=true;
                for(int i=0 ;i<missions.size(); i++){
                    Location.distanceBetween(missions.get(i).getPosition().latitude, missions.get(i).getPosition().longitude,
                            latLng.latitude, latLng.longitude, results);
                    if(results[0]<20){
                        e.onNext(i);
                        noCloseMissions=false;
                    }
                }
                if(noCloseMissions) e.onNext(123);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public void checkPermissions(){
        Dexter.withContext(mContext)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {activeClientProvider();}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }
    public void activeClientProvider(){
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        }
        else {
            Toast.makeText(mContext,"Active Permission to access location",Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<MarkerOptions> prepareMissions(LatLng player) {
        LatLng missionLoc;
        double lat;
        double lng;
        missions.clear();
        for (int i = 0; i < 20; i++) {
            lat = player.latitude - (new Random().nextDouble() * 0.005) + (new Random().nextDouble() * 0.005);
            lng = player.longitude - (new Random().nextDouble() * 0.005) + (new Random().nextDouble() * 0.005);
            missionLoc = new LatLng(lat, lng);

            missions.add(new MarkerOptions().position(missionLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        return missions;
    }



    public void startTimer(int i){
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
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }

    public void signOut(){
        authRepository.signOut();
    }

    public void submitMission(HistoryMission historyMission){
        historyRepository.submitHistory(historyMission);
    }

}