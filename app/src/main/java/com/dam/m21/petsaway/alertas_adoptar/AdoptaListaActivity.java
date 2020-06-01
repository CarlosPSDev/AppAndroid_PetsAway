package com.dam.m21.petsaway.alertas_adoptar;

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
import android.widget.Button;
import android.widget.TextView;

import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_lista.AlertasListaActivity;
import com.dam.m21.petsaway.alertas_lista.ListaAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdoptaListaActivity extends AppCompatActivity {
	RecyclerView rv_la;
	LinearLayoutManager llm;
	ListaAdoptaAdapter la;

	private ArrayList<AlertasList> listaDatos;
	static final String CLAVE_ADOPTA = "ADOPTA";
	static final String CLAVE_FOTOS = "FOTOS";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adopta_lista);
		DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("adoptar");
		listaDatos = new ArrayList<>();
		rv_la = findViewById(R.id.rv_la);
		llm = new LinearLayoutManager(getApplicationContext());
		rv_la.setLayoutManager(llm);
		dbr.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
					AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
					listaDatos.add(alerta);
				}
				la = new ListaAdoptaAdapter(listaDatos);
				rv_la.setAdapter(la);
				la.asignacionOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						AlertasList alerta=listaDatos.get(rv_la.indexOfChild(v));
						Intent intent = new Intent(getApplicationContext(), DetalleAdoptaActivity.class);
						intent.putExtra(CLAVE_ADOPTA, alerta);
						intent.putExtra(CLAVE_FOTOS, alerta.getFotos());
						startActivity(intent);

					}
				});
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				throw databaseError.toException();
			}
		});
	}
	public void addAlertaA(View view) {
		startActivity( new Intent(getApplicationContext(), FormularioAdoptaActivity.class));
	}
}
