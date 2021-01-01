package com.target.runningapp.repositories;

import android.content.Intent;

public interface AuthRepositoryInterface {
    public void createNewAccount(String name,String email,String password);
    public void logIn(String email,String password);
    public void signOut();
    public void checkIfAlreadyLogIn();

}
