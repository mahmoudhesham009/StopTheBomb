package com.target.runningapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.target.runningapp.util.ProgressDialog;
import com.target.runningapp.viewModel.AuthActivityViewModel;
import com.target.runningapp.R;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "Auth";
    Button newAccount,googleAccount;
    AuthActivityViewModel mAuthActivityViewModel;
    int mGoogleRequestCode;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        progressDialog=new ProgressDialog(this);
        mAuthActivityViewModel= new ViewModelProvider(this).get(AuthActivityViewModel.class);
        mAuthActivityViewModel.initAuthActivityViewModel(this);
        newAccount=findViewById(R.id.new_sign_up_button);
        googleAccount=findViewById(R.id.google_sign_up_button);

        mAuthActivityViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser!=null){
                    Toast.makeText(getBaseContext(), "Welcome "+firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), MapActivity.class));
                    finish();
                }
            }
        });

        mAuthActivityViewModel.getLoadingMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.startDialog();
                }else {
                    progressDialog.dismissDialog();
                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CreateAccountActivity.class));
            }
        });

        googleAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mAuthActivityViewModel.logInWithGoogle(),mGoogleRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==mGoogleRequestCode){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mAuthActivityViewModel.firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


}