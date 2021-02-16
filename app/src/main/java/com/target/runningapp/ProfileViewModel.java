package com.target.runningapp;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.model.Profile;
import com.target.runningapp.repositories.HistoryRepository;

import java.util.ArrayList;

public class ProfileViewModel extends ViewModel {
    HistoryRepository historyRepository = new HistoryRepository();
    LiveData<ArrayList<HistoryMission>> historyLiveData = new MutableLiveData<>();
    LiveData<Boolean> updateLiveData = new MutableLiveData<>();
    MutableLiveData<Profile> mProfileMutableLiveData = new MutableLiveData<>();



    public void init() {
        historyRepository.getHistory();
        historyLiveData = historyRepository.getHistoryLiveData();
        updateLiveData = historyRepository.getUpdateLiveData();
        mProfileMutableLiveData=historyRepository.getProfileLiveData();

    }

    public LiveData<Profile> getProfile() {
        return mProfileMutableLiveData;
    }
    public LiveData<ArrayList<HistoryMission>> getHistoryLiveData() {
        return historyLiveData;
    }

    public LiveData<Boolean> getUpdateLiveData() {
        return updateLiveData;
    }

    public void changeImage(Uri data) {
        historyRepository.changeUserPhoto(data);
    }

    public  void getUpdatedProfile(){
        historyRepository.getProfile();
    }

}