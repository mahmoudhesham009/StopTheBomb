package com.target.runningapp;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.Auth;
import com.target.runningapp.repositories.AuthRepository;

public class HomeActivityViewModel extends ViewModel {
    AuthRepository authRepository;
    Context mContext;
    public void init(Context context){
        mContext=context;
        authRepository= AuthRepository.getInstance(mContext);
    }



    public void signOut(){
        authRepository.signOut();
    }
}
