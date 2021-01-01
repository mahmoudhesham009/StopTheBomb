package com.target.runningapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.target.runningapp.R;

public class ProgressDialog {

    Activity activity;
    AlertDialog alertDialog;

    public ProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater= activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
