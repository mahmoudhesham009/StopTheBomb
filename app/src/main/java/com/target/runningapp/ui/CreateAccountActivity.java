package com.target.runningapp.ui;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.target.runningapp.util.Helper;
import com.target.runningapp.R;

public class CreateAccountActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        frameLayout = findViewById(R.id.fragment_container);
        Helper.addFragment(getSupportFragmentManager(), new SignUpFragment(), R.id.fragment_container, "baseFragment");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}