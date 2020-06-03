package com.dam.m21.petsaway.on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.on_boarding.adapter.OnBoardAdapter;

public class LanzadorOnBoard extends AppCompatActivity {
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanzador_onboard);
        viewpager = findViewById(R.id.viewPager);
        OnBoardAdapter oba = new OnBoardAdapter(getSupportFragmentManager());
        viewpager.setAdapter(oba);
    }
}
