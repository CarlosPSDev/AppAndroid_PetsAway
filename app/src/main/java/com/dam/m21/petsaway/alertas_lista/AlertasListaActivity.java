package com.dam.m21.petsaway.alertas_lista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    static final String CLAVE_LIST = "LIST";

    private ArrayList<AlertasList> listaDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_lista);   listaDatos = new ArrayList<>();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("alertas").child("buscado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                    listaDatos.add(alerta);
                }
                rv_la = findViewById(R.id.rv_la);
                llm = new LinearLayoutManager(getApplicationContext());
                la = new ListaAdapter(listaDatos);

                rv_la.setHasFixedSize(true);
                rv_la.setLayoutManager(llm);

                la.asignacionOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DetalleAlerta.class);
                        intent.putExtra(CLAVE_LIST, listaDatos);
                        startActivity(intent);
                    }
                });
                rv_la.setAdapter(la);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        dbr.child("alertas").child("encontrado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                    listaDatos.add(alerta);
                }
                rv_la = findViewById(R.id.rv_la);
                llm = new LinearLayoutManager(getApplicationContext());
                la = new ListaAdapter(listaDatos);

                rv_la.setHasFixedSize(true);
                rv_la.setLayoutManager(llm);

                la.asignacionOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = rv_la.indexOfChild(v);
                        Intent intent = new Intent(getApplicationContext(), DetalleAlerta.class);
                        intent.putExtra(CLAVE_LIST, listaDatos.get(i));
                        startActivity(intent);
                    }
                });
                rv_la.setAdapter(la);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void addAlerta(View view) {
        Intent intent = new Intent(getApplicationContext(), AlertasMapaActivity.class);
        startActivity(intent);
    }
}
