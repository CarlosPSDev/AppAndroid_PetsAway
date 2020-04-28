package com.dam.m21.petsaway.perfil_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.dam.m21.petsaway.R;

import java.util.ArrayList;

public class PerfilUsuario extends AppCompatActivity {
    ArrayList<PojoPruebasCarlos> listaMascotas;
    RecyclerView rv;
    AdapterPetsProfile adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
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
    }
}
