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

    private ArrayList<AlertasList> listaDatos;

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
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                    listaDatos.add(alerta);
                }
                la = new ListaAdapter(listaDatos);
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
        intent.putExtra(CLAVE_ADD,"ADD");
        finish();
        startActivity(intent);
    }
}
