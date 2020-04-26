package com.dam.m21.petsaway.onBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dam.m21.petsaway.R;

public class ActivityLanzador extends AppCompatActivity {
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanzador);

        viewpager = findViewById(R.id.viewPager);
        OnBoardAdapter oba = new OnBoardAdapter(getSupportFragmentManager());
        viewpager.setAdapter(oba);

    }
}
