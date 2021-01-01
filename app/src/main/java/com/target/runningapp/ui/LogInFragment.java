package com.target.runningapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.target.runningapp.util.Helper;
import com.target.runningapp.viewModel.LogInViewModel;
import com.target.runningapp.util.ProgressDialog;
import com.target.runningapp.R;

public class LogInFragment extends Fragment {

    private LogInViewModel mViewModel;
    private EditText email, password;
    TextView createAccount;
    private Button logIn;
    ProgressDialog progressDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog= new ProgressDialog(getActivity());
        mViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        mViewModel.init(getActivity());
        mViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Toast.makeText(getActivity(), "Welcome, " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MapActivity.class));
                    getActivity().finishAffinity();
                }
            }
        });

        mViewModel.getLoadingMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.startDialog();
                }else {
                    progressDialog.dismissDialog();
                }
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        createAccount = v.findViewById(R.id.createNewAccount);
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        logIn = v.findViewById(R.id.login_button);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.logIn(email.getText().toString(), password.getText().toString());
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(getActivity().getSupportFragmentManager(), new SignUpFragment(), R.id.fragment_container, "signUp");
            }
        });
        return v;

    }

}