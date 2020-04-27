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

public class OnBoard1Fragment extends Fragment {
    ImageView ivSiguiente;
    ViewPager viewPager;

    ImageView ivFoto;
    TextView tvTitulo;
    TextView tvDesc;

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

        ivFoto = view.findViewById(R.id.ivSlide1);
        tvTitulo = view.findViewById(R.id.tvSl1Tit);
        tvDesc  =view.findViewById(R.id.tvSl1Desc);

        final Animation animImg = AnimationUtils.loadAnimation(getContext(), R.anim.slidedown_slide);
        ivFoto.startAnimation(animImg);

        final Animation animTit = AnimationUtils.loadAnimation(getContext(), R.anim.leftin_slide);
        tvTitulo.startAnimation(animTit);

        final Animation animDesc = AnimationUtils.loadAnimation(getContext(), R.anim.rightin_slide);
        tvDesc.startAnimation(animDesc);

        ivSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(1);

                ivFoto = viewPager.findViewById(R.id.ivSlide2);
                ivFoto.startAnimation(animImg);

                tvTitulo = viewPager.findViewById(R.id.tvSl2Tit);
                tvTitulo.startAnimation(animTit);

                tvDesc = viewPager.findViewById(R.id.tvSl2Desc);
                tvDesc.startAnimation(animDesc);

           }
       });
        return  view;
    }
    
}
