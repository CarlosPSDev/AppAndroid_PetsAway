package com.dam.m21.petsaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity {
    EditText tipoAletra,tipoAnimal,color,fecha,raza,desc;
    private double latitud,longitud;

    Map<String, Object> ubiPunto;


    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        tipoAletra=findViewById(R.id.tipoAletra);
        tipoAnimal=findViewById(R.id.tipoAnimal);
        color=findViewById(R.id.color);
        fecha=findViewById(R.id.fecha);
        raza=findViewById(R.id.raza);
        desc=findViewById(R.id.desc);

    }

    public void guardar(View view) {
        if(!tipoAletra.getText().toString().isEmpty()&&!tipoAnimal.getText().toString().isEmpty()&&!color.getText().toString().isEmpty()) {
            latitud = getIntent().getExtras().getDouble(AlertasMapaActivity.CLAVE_LAT, 1);
            longitud = getIntent().getExtras().getDouble(AlertasMapaActivity.CLAVE_LONG, 1);
            Date fechaAct = new Date();
            String fPush = fechaAct.toString();
            ubiPunto = new HashMap<>();
            ubiPunto.put("latitude", latitud);
            ubiPunto.put("longitude", longitud);
            ubiPunto.put("tipoAletra", tipoAletra.getText().toString());
            ubiPunto.put("tipoAnimal", tipoAnimal.getText().toString());
            ubiPunto.put("color", color.getText().toString());
            ubiPunto.put("fecha", fecha.getText().toString());
            ubiPunto.put("raza", raza.getText().toString());
            ubiPunto.put("desc", desc.getText().toString());
            ubiPunto.put("fPush", fPush);
            ubiPunto.put("userPush", "userPush");
            String tA="";
            if(tipoAletra.getText().toString().equals("encontrados")){
                tA="encontrados";
            }else if(tipoAletra.getText().toString().equals("buscados")){
                tA="buscados";
            }
            dbr = FirebaseDatabase.getInstance().getReference();
            dbr.child("alertas").child(tA).push().setValue(ubiPunto);
            startActivity(new Intent(getApplicationContext(), AlertasMapaActivity.class));
        }else{
            Toast.makeText(getApplicationContext(),R.string.toast_faltanDatos, Toast.LENGTH_LONG).show();

        }
    }

}
