package com.dam.m21.petsaway.perfil_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dam.m21.petsaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

public class PerfilUsuario extends AppCompatActivity {
    TextView tvNomUsuario;
    ArrayList<PojoPruebasCarlos> listaMascotas;
    RecyclerView rv;
    AdapterPetsProfile adapter;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        tvNomUsuario = findViewById(R.id.tvValNomUsuario);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        String nombreUsuario = fbUser.getUid();
        tvNomUsuario.setText(nombreUsuario);

        rv = findViewById(R.id.recyclerProfilPets);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        listaMascotas = new ArrayList<>();
        listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));
        /*listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));
        listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));*/

        adapter = new AdapterPetsProfile(listaMascotas);
        rv.setAdapter(adapter);

    }

    public void modificarPerfil(View view) {
        //TODO llamar Fragment
    }

    public void agregarMascota(View view) {
    }
}
