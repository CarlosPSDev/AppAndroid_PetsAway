package com.dam.m21.petsaway.onBoarding;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dam.m21.petsaway.R;

public class OnBoard1Fragment extends Fragment {
    ImageView ivSiguiente;
    ViewPager viewPager;

    public OnBoard1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_onboard1, container, false);
       viewPager = getActivity().findViewById(R.id.viewPager);
       ivSiguiente = view.findViewById(R.id.ivSl1Sig);

       ivSiguiente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                viewPager.setCurrentItem(1);
           }
       });
       return  view;

    }
    
}
