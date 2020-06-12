package com.dam.m21.petsaway.alertas_lista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.m21.petsaway.R;

import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlertasListaActivity extends AppCompatActivity {
    RecyclerView rv_la;
    LinearLayoutManager llm;
    ListaAdapter la;

    private ArrayList<AlertasList> listaDatos;
    AlertDialog.Builder ad;

    static final String CLAVE_ADD = "ADD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_lista);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("alertas");
        listaDatos = new ArrayList<>();
        rv_la = findViewById(R.id.rv_alertas);
        llm = new LinearLayoutManager(getApplicationContext());
        rv_la.setLayoutManager(llm);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rv_la.clearOnChildAttachStateChangeListeners();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                    listaDatos.add(alerta);
                }
                la = new ListaAdapter(listaDatos);
                rv_la.setAdapter(la);
                la.asignacionOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertasList alerta=listaDatos.get(rv_la.indexOfChild(v));
                        LayoutInflater LI=LayoutInflater.from(AlertasListaActivity.this);
                        final View vista=LI.inflate(R.layout.detalle_alerta,null);
                        ad = new AlertDialog.Builder(AlertasListaActivity.this);
                        ad.setCancelable(false);
                        final AlertDialog adC=ad.create();

                        ImageButton bt_close_dialog=vista.findViewById(R.id.bt_close_dialog);
                        ImageView detalle_tipoAletra=vista.findViewById(R.id.detalle_tipoAletra);
                        TextView detalle_tipoAnimal=vista.findViewById(R.id.detalle_tipoAnimal);
                        TextView detalle_color=vista.findViewById(R.id.detalle_color);
                        //TextView detalle_fecha=vista.findViewById(R.id.detalle_fecha);
                        TextView detalle_raza=vista.findViewById(R.id.detalle_raza);
                        TextView detalle_desc=vista.findViewById(R.id.detalle_desc);
                        TextView detalle_fPush=vista.findViewById(R.id.detalle_fPush);
                        TextView detalle_userPush=vista.findViewById(R.id.detalle_userPush);

                        if (alerta.getTipoAletra().equalsIgnoreCase("buscado")) {
                            detalle_tipoAletra.setImageResource(R.drawable.ic_logo_perdido_mapa);
                        } else {
                            detalle_tipoAletra.setImageResource(R.drawable.ic_logo_encontrado_mapa);
                        }

                        detalle_tipoAnimal.setText(alerta.getTipoAnimal());
                        detalle_color.setText(alerta.getColor());
                        //detalle_fecha.setText(alerta.getFecha());
                        detalle_raza.setText(alerta.getRaza());
                        detalle_desc.setText(alerta.getDesc());
                        detalle_fPush.setText(alerta.getfPush());
                        detalle_userPush.setText(alerta.getUserPush());
                        bt_close_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adC.cancel();
                            }
                        });
                        if(vista.getParent() != null) {
                            ((ViewGroup)vista.getParent()).removeView(vista);
                        }
                        adC.setView(vista);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }

    public void addAlerta(View view) {
        Intent intent = new Intent(getApplicationContext(), AlertasMapaActivity.class);
        intent.putExtra(CLAVE_ADD,"ADD");
        startActivity(intent);
    }
}
