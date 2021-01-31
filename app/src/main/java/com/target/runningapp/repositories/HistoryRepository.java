package com.target.runningapp.repositories;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.target.runningapp.model.HistoryMission;

public class HistoryRepository {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;

    public HistoryRepository(){
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("History");
    }

    public static HistoryRepository getInstance() {
        HistoryRepository instance;
        instance = new HistoryRepository();
        return instance;
    }

    public void submitHistory(HistoryMission history) {
        mDatabase.child(FirebaseAuth.getInstance().getUid()).push().setValue(history);

    }


}
