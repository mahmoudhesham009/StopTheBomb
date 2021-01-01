package com.target.runningapp.repositories;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthRepository implements AuthRepositoryInterface {
    MutableLiveData<FirebaseUser> userMutableLiveData;
    MutableLiveData<Boolean> checkMutableLiveData;
    MutableLiveData<Boolean> loadingMutableLiveData;
    FirebaseAuth mAuth;
    Context mContext;


    public AuthRepository(Context context) {
        mContext = context;
        userMutableLiveData = new MutableLiveData<>();
        checkMutableLiveData = new MutableLiveData<>();
        loadingMutableLiveData=new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthRepository getInstance(Context context) {
        AuthRepository instance;
        instance = new AuthRepository(context);
        return instance;
    }


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getCheckMutableLiveData() {
        return checkMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoadingMutableLiveData() {
        return loadingMutableLiveData;
    }

    @Override
    public void createNewAccount(final String name, String email, String password) {
        if (!email.equals("") && !password.equals("") && !name.trim().equals("")) {
            loadingMutableLiveData.postValue(true);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener((Activity) mContext, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userMutableLiveData.postValue(mAuth.getCurrentUser());
                                loadingMutableLiveData.postValue(false);
                            }
                        });

                    } else {
                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        loadingMutableLiveData.postValue(false);
                    }
                }
            });

        } else {
            Toast.makeText(mContext, "Complete your information", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void logIn(String email, String password) {
        if (!email.equals("") && !password.equals("")) {
            loadingMutableLiveData.postValue(true);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userMutableLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingMutableLiveData.postValue(false);
                }
            });
        }
    }

    @Override
    public void signOut() {
        mAuth.signOut();
        userMutableLiveData.postValue(null);
    }

    @Override
    public void checkIfAlreadyLogIn() {
        Boolean check = mAuth.getCurrentUser() != null;
        checkMutableLiveData.postValue(check);
    }

}
