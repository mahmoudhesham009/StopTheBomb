package com.target.runningapp.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Helper {
    public static void addFragment(FragmentManager fm, Fragment fragment, int id, String name){
        FragmentManager fragmentManager=fm;
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(id,fragment)
                .addToBackStack(name)
                .commit();
    }

    public static void replaceFragment(FragmentManager fm, Fragment fragment, int id, String name){
        FragmentManager fragmentManager=fm;
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(id,fragment)
                .addToBackStack(name)
                .commit();
    }

    public static void popFragment(FragmentManager fm, String name){
        FragmentManager fragmentManager=fm;
        fm.popBackStack(name,0);

    }


}
