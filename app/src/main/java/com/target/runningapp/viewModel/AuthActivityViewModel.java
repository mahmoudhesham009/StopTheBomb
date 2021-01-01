package com.target.runningapp.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.target.runningapp.R;
import com.target.runningapp.repositories.AuthRepository;


public class AuthActivityViewModel extends ViewModel {
    Context mContext;
    MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    FirebaseAuth mAuth;
    GoogleSignInOptions mGoogleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;
    MutableLiveData<Boolean> loadingMutableLiveData;


    public MutableLiveData<Boolean> getLoadingMutableLiveData() {
        return loadingMutableLiveData;
    }

    public void initAuthActivityViewModel(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        firebaseUserMutableLiveData = new MutableLiveData<>();
        loadingMutableLiveData=new MutableLiveData<>();
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, mGoogleSignInOptions);
    }


    public LiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }


    public Intent logInWithGoogle() {
        return mGoogleSignInClient.getSignInIntent();
    }


    public void firebaseAuthWithGoogle(String idToken) {
        loadingMutableLiveData.postValue(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            loadingMutableLiveData.postValue(false);
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseUserMutableLiveData.postValue(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            loadingMutableLiveData.postValue(false);
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
