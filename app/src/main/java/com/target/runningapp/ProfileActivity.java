package com.target.runningapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.target.runningapp.adapter.HistoryRecyclerViewAdapter;
import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.model.Profile;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    ProfileViewModel mViewModel;
    TextView userName,note,level;
    ImageView profilePic,addPic;
    CircularProgressBar xp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mViewModel= new ViewModelProvider(this).get(ProfileViewModel.class);
        recyclerView=findViewById(R.id.history_recycle_view);
        profilePic=findViewById(R.id.profile_pic);
        addPic=findViewById(R.id.addPic);
        userName=findViewById(R.id.username);
        note=findViewById(R.id.noHistory);
        level=findViewById(R.id.level);
        xp=findViewById(R.id.circularProgressBar);
        mViewModel.init();
        mViewModel.getUpdatedProfile();
        mViewModel.getHistoryLiveData().observe(this, new Observer<ArrayList<HistoryMission>>() {
            @Override
            public void onChanged(ArrayList<HistoryMission> historyMissions) {
                Collections.reverse(historyMissions);
                historyRecyclerViewAdapter=new HistoryRecyclerViewAdapter(historyMissions);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(historyRecyclerViewAdapter);
                if(historyMissions.size()<=0)
                    note.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getUpdateLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Glide.with(ProfileActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profilePic);

            }
        });
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null)
            Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profilePic);

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),502);
            }
        });

        mViewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                level.setText("L. "+profile.getLevel());
                xp.setProgress(profile.getXp());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==502){
            if(data!=null){
                mViewModel.changeImage(data.getData());
            }

        }
    }
}