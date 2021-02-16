package com.target.runningapp.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.target.runningapp.model.HistoryMission;
import com.target.runningapp.model.Profile;

import java.util.ArrayList;

public class HistoryRepository {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    MutableLiveData<ArrayList<HistoryMission>> historyLiveData=new MutableLiveData<>();
    MutableLiveData<Boolean> updateLiveData=new MutableLiveData<>();
    MutableLiveData<Profile> mProfileMutableLiveData = new MutableLiveData<>();




    public HistoryRepository(){
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("History");
    }

    public static HistoryRepository getInstance() {
        HistoryRepository instance;
        instance = new HistoryRepository();
        return instance;
    }

    public void getHistory(){
        final ArrayList<HistoryMission> historyMissions=new ArrayList<>();
        mDatabase.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    historyMissions.add(postSnapshot.getValue(HistoryMission.class));
                }
                historyLiveData.postValue(historyMissions);
                Log.d("firefire","in call back");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firefire","cancel call back");
            }
        });

    }

    public LiveData<ArrayList<HistoryMission>> getHistoryLiveData() {
        return historyLiveData;
    }

    public void submitHistory(HistoryMission history) {
        mDatabase.child(FirebaseAuth.getInstance().getUid()).push().setValue(history);
    }

    public void changeUserPhoto(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(uri.toString()))
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateLiveData.postValue(true);
            }
        });
    }

    public LiveData<Boolean> getUpdateLiveData() {
        return updateLiveData;
    }
    public MutableLiveData<Profile> getProfileLiveData() {
        return mProfileMutableLiveData;
    }

    public void getProfile(){
        firebaseDatabase.getReference("profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Profile profile=dataSnapshot.getValue(Profile.class);
                    mProfileMutableLiveData.postValue(profile);
                }else {
                    firebaseDatabase.getReference("profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Profile(1,0));
                    mProfileMutableLiveData.postValue(new Profile(1,0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateProfile(Profile profile){
        if(profile != null){
            if(profile.getXp()>=100){
                profile.setXp(0);
                profile.setLevel(profile.getLevel()+1);
            }else{
                profile.setXp(profile.getXp()+10);

            }
            firebaseDatabase.getReference("profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(profile);
        }

    }
}
