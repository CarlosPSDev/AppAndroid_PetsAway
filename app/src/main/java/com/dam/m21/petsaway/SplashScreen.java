package com.dam.m21.petsaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        getSupportActionBar().hide();

        View tvTitulo = findViewById(R.id.tvTituloSplash);
        ImageView ivLogo = findViewById(R.id.ivLogoSplash);

        Animation translate1 = AnimationUtils.loadAnimation(this, R.anim.emerger_y_escalar);
        tvTitulo.startAnimation(translate1);

        Animation translate2 = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        ivLogo.startAnimation(translate2);

        openApp(true);
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen
                        .this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.leftin, R.anim.leftout);
                finish();
            }
        }, 3750);
    }
}
