package com.dam.m21.petsaway.onBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.dam.m21.petsaway.R;

public class LanzadorOnBoard extends AppCompatActivity {
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanzador_onboard);
        /*Este bloque debería descomentarse al lanzar la app final. También podría ir tras validar el login
        para que compruebe si hay q llamar a este activity o directamente no*/
      /*  SharedPreferences sharedPrefs = getSharedPreferences("ArchivoVeces", MODE_PRIVATE);
        int numVeces = sharedPrefs.getInt("vecesEjecutado", 0);

        if (numVeces > 0) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            numVeces++;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("vecesEjecutado", numVeces);
            editor.commit();*/

            viewpager = findViewById(R.id.viewPager);
            OnBoardAdapter oba = new OnBoardAdapter(getSupportFragmentManager());
            viewpager.setAdapter(oba);
        //}

    }
}
