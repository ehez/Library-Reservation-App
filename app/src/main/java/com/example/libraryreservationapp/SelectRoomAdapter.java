package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SelectRoomAdapter extends FragmentPagerAdapter {
    public SelectRoomAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return Step1Fragment.getInstance();
            case 1:
                return Step2Fragment.getInstance();
            case 2:
                return Step3Fragment.getInstance();
            case 3:
                return Step4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
