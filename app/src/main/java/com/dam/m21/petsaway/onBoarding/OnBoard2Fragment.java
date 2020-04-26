package com.dam.m21.petsaway.onBoarding;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.m21.petsaway.R;

public class OnBoard2Fragment extends Fragment {
    ImageView ivSiguiente;
    ImageView ivAnterior;
    ViewPager viewPager;


    public OnBoard2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboard2, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        ivSiguiente = view.findViewById(R.id.ivFlechaDerecha);
        ivAnterior = view.findViewById(R.id.ivFlechaIzquierda);

        ivSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        ivAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        return view;
    }

}
