package com.dam.m21.petsaway.on_boarding.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dam.m21.petsaway.on_boarding.fragmentos.OnBoard1Fragment;
import com.dam.m21.petsaway.on_boarding.fragmentos.OnBoard2Fragment;
import com.dam.m21.petsaway.on_boarding.fragmentos.OnBoard3Fragment;

public class OnBoardAdapter extends FragmentPagerAdapter {

    public OnBoardAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnBoard1Fragment();
            case 1:
                return new OnBoard2Fragment();
            case 2:
                return new OnBoard3Fragment();
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
