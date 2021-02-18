package com.target.runningapp.viewModel;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.target.runningapp.service.RunService;
import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.model.Profile;
import com.target.runningapp.model.StopBombMission;
import com.target.runningapp.repositories.AuthRepository;
import com.target.runningapp.repositories.HistoryRepository;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {
    Context mContext;

    AuthRepository authRepository;
    HistoryRepository historyRepository;

    ArrayList<MarkerOptions> missions = new ArrayList<>();
    MutableLiveData<RunService.MyBinder> mBinder = new MutableLiveData<>();
    MutableLiveData<Profile> mProfileMutableLiveData;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder.postValue((RunService.MyBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder.postValue(null);
        }
    };


    public void init(Context context) {
        this.mContext = context;
        authRepository = AuthRepository.getInstance(context);
        historyRepository = HistoryRepository.getInstance();
        mProfileMutableLiveData=historyRepository.getProfileLiveData();
    }


    public LiveData<RunService.MyBinder> getBindLiveData() {
        return mBinder;
    }

    public LiveData<Profile> getProfile() {
        return mProfileMutableLiveData;
    }


    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }


    public Observable<Integer> checkCloseMission(final LatLng latLng) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                float[] results = new float[1];
                boolean noCloseMissions = true;
                for (int i = 0; i < missions.size(); i++) {
                    Location.distanceBetween(missions.get(i).getPosition().latitude, missions.get(i).getPosition().longitude,
                            latLng.latitude, latLng.longitude, results);
                    if (results[0] < 20) {
                        e.onNext(i);
                        noCloseMissions = false;
                    }
                }
                if (noCloseMissions) e.onNext(123);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Integer> checkCloseBomb(final LatLng latLng) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                float[] results = new float[1];
                boolean noCloseMissions = true;
                for (int i = 0; i < missions.size(); i++) {
                    Location.distanceBetween(missions.get(i).getPosition().latitude, missions.get(i).getPosition().longitude,
                            latLng.latitude, latLng.longitude, results);
                    if (results[0] < 20) {
                        e.onNext(i);
                        noCloseMissions = false;
                    }
                }
                if (noCloseMissions) e.onNext(123);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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


    public void signOut() {
        authRepository.signOut();
    }

    public void submitMission(HistoryMission historyMission) {
        historyRepository.submitHistory(historyMission);
    }

    public StopBombMission getStopBombMission(Profile profile) {

        switch (profile.getLevel()) {
            case 1:
                return new StopBombMission(10, 1, 600, 400);
            case 2:
                return new StopBombMission(13, 1, 800, 600);
            case 3:
                return new StopBombMission(15, 2, 500, 300);
            case 4:
                return new StopBombMission(15, 2, 600, 400);
            case 5:
                return new StopBombMission(10, 1, 1000, 800);
            case 6:
                return new StopBombMission(10, 2, 600, 400);
            case 7:
                return new StopBombMission(15, 3, 400, 300);
            case 8:
                return new StopBombMission(15, 3, 600, 400);
            default:
                return new StopBombMission(15, 3, 700, 400);
        }
    }

    public  void getUpdatedProfile(){
        historyRepository.getProfile();
    }

    public  void updateProfile(Profile profile){
        historyRepository.updateProfile(profile);
    }

}