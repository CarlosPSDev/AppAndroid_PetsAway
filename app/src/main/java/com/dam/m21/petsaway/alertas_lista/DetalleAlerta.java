package com.dam.m21.petsaway.alertas_lista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetalleAlerta extends AppCompatActivity {

    TextView detalle_tipoAletra,detalle_tipoAnimal,detalle_color,detalle_fecha,detalle_raza,detalle_desc,detalle_fPush,detalle_userPush;
    ImageView detalle_idFoto;
    private Double latitude,longitude;
    static final String CLAVE_LAT = "LAT_A";
    static final String CLAVE_LON = "LON_A";
    static final String CLAVE_TA = "TA";

    AlertasList alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alerta);


        detalle_tipoAletra=findViewById(R.id.detalle_tipoAletra);
        detalle_tipoAnimal=findViewById(R.id.detalle_tipoAnimal);
        detalle_color=findViewById(R.id.detalle_color);
        detalle_fecha=findViewById(R.id.detalle_fecha);
        detalle_raza=findViewById(R.id.detalle_raza);
        detalle_desc=findViewById(R.id.detalle_desc);
        detalle_fPush=findViewById(R.id.detalle_fPush);
        detalle_userPush=findViewById(R.id.detalle_userPush);
        detalle_idFoto=findViewById(R.id.detalle_idFoto);
        alerta = getIntent().getParcelableExtra(AlertasListaActivity.CLAVE_LIST);

        StorageReference msr= FirebaseStorage.getInstance().getReference();
        StorageReference sr = msr.child("fotosAnimales").child(alerta.getIdFoto());
        sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(detalle_idFoto);
            }
        });
        detalle_tipoAletra.setText(alerta.getTipoAletra());
        detalle_tipoAnimal.setText(alerta.getTipoAnimal());
        detalle_color.setText(alerta.getColor());
        detalle_fecha.setText(alerta.getFecha());
        detalle_raza.setText(alerta.getRaza());
        detalle_desc.setText(alerta.getDesc());
        detalle_fPush.setText(alerta.getfPush());
        detalle_userPush.setText(alerta.getUserPush());
    }

    public void verEnMapa(View view) {
        Intent intent = new Intent(getApplicationContext(), AlertasMapaActivity.class);
        latitude=alerta.getLatitude();
        longitude=alerta.getLongitude();
        intent.putExtra(CLAVE_LAT, latitude);
        intent.putExtra(CLAVE_LON, longitude);
        intent.putExtra(CLAVE_TA, alerta.getTipoAletra());
        startActivity(intent);
    }
}
