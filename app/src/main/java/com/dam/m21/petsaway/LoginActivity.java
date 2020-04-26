package com.dam.m21.petsaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dam.m21.petsaway.onBoarding.LanzadorOnBoard;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void contraseniaOlvidada(View view) {
    }

    public void accesoRegistro(View view) {
    }

    public void accesoAplicacion(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void loginGmail(View view) {
    }

    public void loginFacebook(View view) {
    }

    public void abrirOnBoarding(View view) {
        Intent i = new Intent(this, LanzadorOnBoard.class);
        startActivity(i);
    }
}
