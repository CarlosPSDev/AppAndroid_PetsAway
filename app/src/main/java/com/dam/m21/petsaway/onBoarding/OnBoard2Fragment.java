package com.dam.m21.petsaway.onBoarding;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.m21.petsaway.R;

public class OnBoard2Fragment extends Fragment {
    ImageView ivSiguiente;
    ImageView ivAnterior;
    ViewPager viewPager;

    ImageView ivFoto;
    TextView tvTitulo;
    TextView tvDesc;

    public OnBoard2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboard2, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        ivSiguiente = view.findViewById(R.id.ivSl2Sig);
        ivAnterior = view.findViewById(R.id.ivSl2Ant);

        ivFoto = view.findViewById(R.id.ivSlide2);
        tvTitulo = view.findViewById(R.id.tvSl2Tit);
        tvDesc  =view.findViewById(R.id.tvSl2Desc);

        final Animation animImg = AnimationUtils.loadAnimation(getContext(), R.anim.slidedown_slide);
        final Animation animTit = AnimationUtils.loadAnimation(getContext(), R.anim.leftin_slide);
        final Animation animDesc = AnimationUtils.loadAnimation(getContext(), R.anim.rightin_slide);

        ivSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);

                ivFoto = viewPager.findViewById(R.id.ivSlide3);
                ivFoto.startAnimation(animImg);

                tvTitulo = viewPager.findViewById(R.id.tvSl3Tit);
                tvTitulo.startAnimation(animTit);

                tvDesc = viewPager.findViewById(R.id.tvSl3Desc);
                tvDesc.startAnimation(animDesc);
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
