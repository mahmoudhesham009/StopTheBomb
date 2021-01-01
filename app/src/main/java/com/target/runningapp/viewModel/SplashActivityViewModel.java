package com.target.runningapp.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.target.runningapp.repositories.AuthRepository;

public class SplashActivityViewModel extends ViewModel {
    private AuthRepository authRepository;
    private Context mContext;
    private MutableLiveData<Boolean> checkMutableLiveData;

    public void init(Context context){
        mContext=context;
        authRepository=AuthRepository.getInstance(mContext);
        checkMutableLiveData=authRepository.getCheckMutableLiveData();
    }

    public LiveData<Boolean> getCheckMutableLiveData() {
        return checkMutableLiveData;
    }

    public void checkIfAlreadyLogIn(){
        authRepository.checkIfAlreadyLogIn();
    }

}
