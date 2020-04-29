package com.dam.m21.petsaway.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        View tvTitulo = findViewById(R.id.tvTituloSplash);
        ImageView ivLogo = findViewById(R.id.ivLogoSplash);

        Animation animacionTitulo = AnimationUtils.loadAnimation(this, R.anim.emerger_y_escalar);
        tvTitulo.startAnimation(animacionTitulo);

        Animation animacionLogo = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        ivLogo.startAnimation(animacionLogo);

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
