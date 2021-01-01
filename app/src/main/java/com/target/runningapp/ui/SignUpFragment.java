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
import com.target.runningapp.util.ProgressDialog;
import com.target.runningapp.R;
import com.target.runningapp.viewModel.SignUpViewModel;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private EditText name, email, password;
    TextView haveAccount;
    private Button signUp;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this.getActivity());
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        mViewModel.init(getActivity());
        mViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Toast.makeText(getActivity(), "Welcome, " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), IntroSliderActivity.class));
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
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name= v.findViewById(R.id.name);
        signUp = v.findViewById(R.id.sign_up);
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        haveAccount=v.findViewById(R.id.haveAccount);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.createNewAccount(name.getText().toString(),email.getText().toString(), password.getText().toString());
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(getActivity().getSupportFragmentManager(),new LogInFragment(),R.id.fragment_container,"logIn");
            }
        });
        return v;
    }


}