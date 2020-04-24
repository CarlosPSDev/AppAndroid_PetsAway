package com.dam.m21.petsaway.onBoarding;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dam.m21.petsaway.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnBoard2Fragment extends Fragment {
    TextView tvSiguiente;
    TextView tvAnterior;
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
        tvSiguiente = view.findViewById(R.id.tvSl2Sig);
        tvAnterior = view.findViewById(R.id.tvSl2Ant);

        tvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        tvAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        return view;
    }

}
