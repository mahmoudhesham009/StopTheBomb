package com.target.runningapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.target.runningapp.R;
import com.target.runningapp.viewModel.SplashActivityViewModel;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final SplashActivityViewModel viewModel = new ViewModelProvider(this).get(SplashActivityViewModel.class);
        viewModel.init(this);

        viewModel.getCheckMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean == true) {
                    //already login
                    intent = new Intent(getBaseContext(), MapActivity.class);
                } else {
                    //need login
                    intent = new Intent(getBaseContext(), AuthActivity.class);
                }

            }
        });

        viewModel.checkIfAlreadyLogIn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3750);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
}