package com.dam.m21.petsaway.perfil_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dam.m21.petsaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PerfilUsuario extends AppCompatActivity {
    EditText etNombre;
    EditText etCiudad;
    TextView tvEmail;
    Button btnEditarP;
    Button btnAgregarPet;
    Button btnGuardarCambios;
    ArrayList<PojoPruebasCarlos> listaMascotas;
    RecyclerView rv;
    AdapterPetsProfile adapter;
    
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        etNombre = findViewById(R.id.etValNomUsuario);
        etCiudad = findViewById(R.id.etValCiudadUsuario);
        btnEditarP = findViewById(R.id.btnModifPerfil);
        btnAgregarPet = findViewById(R.id.btnAgregarMasc);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        tvEmail = findViewById(R.id.tvValEmailUsuario);
        rv = findViewById(R.id.recyclerProfilPets);

        deshabilitarEditext(true);
        //TODO Recopilar el nombre del usuario según si el login ha sido con firebase, facebook o google

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        /*user user = datasnapshot.getValue(User.Class);
        username.setText(user.getUsername);*/

        String nombreUsuario = fbUser.getDisplayName();
        etNombre.setText(nombreUsuario);


        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        listaMascotas = new ArrayList<>();
        listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));
        listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));
        listaMascotas.add(new PojoPruebasCarlos("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoPruebasCarlos("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoPruebasCarlos("Luna", String.valueOf(R.drawable.slide_three_img)));

        adapter = new AdapterPetsProfile(listaMascotas);
        rv.setAdapter(adapter);

    }

    public void modificarPerfil(View view) {
        deshabilitarEditext(false);
    }

    public void guardarCambios(View view) {
        deshabilitarEditext(true);
    }

    public void agregarMascota(View view) {
    }

    private void deshabilitarEditext(Boolean hd){
        etNombre.setFocusable(!hd);
        etNombre.setFocusableInTouchMode(!hd);
        etNombre.setClickable(!hd);
        etCiudad.setFocusable(!hd);
        etCiudad.setFocusableInTouchMode(!hd);
        etCiudad.setClickable(!hd);
        tvEmail.setEnabled(hd);
        btnAgregarPet.setEnabled(hd);
        btnEditarP.setEnabled(hd);
        btnGuardarCambios.setEnabled(!hd);
        rv.setEnabled(hd);
        if (hd) {
            btnEditarP.setVisibility(View.VISIBLE);
            btnAgregarPet.setVisibility(View.VISIBLE);
            btnGuardarCambios.setVisibility(View.INVISIBLE);
            etNombre.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.VISIBLE);
        } else{
            btnEditarP.setVisibility(View.INVISIBLE);
            btnAgregarPet.setVisibility(View.INVISIBLE);
            btnGuardarCambios.setVisibility(View.VISIBLE);
            etNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.GONE);
        }
    }



}
