package com.target.runningapp.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.target.runningapp.repositories.AuthRepository;

public class SignUpViewModel extends ViewModel {
    private AuthRepository authRepository;
    private Context mContext;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> loadingMutableLiveData;


    public void init(Context context) {
        mContext = context;
        authRepository = AuthRepository.getInstance(mContext);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        loadingMutableLiveData= authRepository.getLoadingMutableLiveData();
    }

    public void createNewAccount(String name, String email, String password) {
        authRepository.createNewAccount(name, email, password);
    }

    public LiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoadingMutableLiveData() {
        return loadingMutableLiveData;
    }
}