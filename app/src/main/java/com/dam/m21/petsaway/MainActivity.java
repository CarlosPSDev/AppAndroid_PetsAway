package com.dam.m21.petsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.dam.m21.petsaway.acerca_de.AcercaDeActivity;
import com.dam.m21.petsaway.alertas_adoptar.AdoptaListaActivity;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_lista.AlertasListaActivity;
import com.dam.m21.petsaway.ajustes.AjustesActivity;
import com.dam.m21.petsaway.alertas_lista.ListaAdapter;
import com.dam.m21.petsaway.chat.MainActivityChat;
import com.dam.m21.petsaway.login.LoginActivity;
import com.dam.m21.petsaway.aviso_legal.AvisoLegal;
import com.dam.m21.petsaway.perfil_usuario.PerfilUsuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Implementaciones Navigation Drawer !
    DrawerLayout drawer;
    ActionBarDrawerToggle toogle;
    NavigationView navController;

    private FirebaseAuth fbAuth;
    static final String CLAVE_EMAIL = "EMAIL";

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        drawer.addDrawerListener(toogle);
        toogle.syncState();

        ImageButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        navController = findViewById(R.id.nav_view);
        navController.setNavigationItemSelectedListener(this);

        navController.setItemIconTintList(null);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // ============================================================ //
    }
    /**
     * Este método se utiliza para la manipulación del open and close del DrawerLayout
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    /**
     * En este método controlamos los diferentes eventos del NavigationDrawer
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_perfil){
            startActivity(new Intent(MainActivity.this, PerfilUsuario.class));

        } else if (id == R.id.nav_alerta){
            startActivity(new Intent(MainActivity.this, AlertasListaActivity.class));

        } else if (id == R.id.nav_adoptar){
            startActivity(new Intent(MainActivity.this, AdoptaListaActivity.class));

        } else if (id == R.id.nav_chat){
            startActivity(new Intent(MainActivity.this, MainActivityChat.class));

        } else if (id == R.id.nav_noticias){
            startActivity(new Intent(MainActivity.this, NoticiasActivity.class));

        } else if (id == R.id.nav_ajustes){
            startActivity(new Intent(MainActivity.this, AjustesActivity.class));

        } else if (id == R.id.nav_aviso_legal){
            startActivity(new Intent(MainActivity.this, AvisoLegal.class));

        } else if (id == R.id.nav_acerca_de){
            startActivity(new Intent(MainActivity.this, AcercaDeActivity.class));

        } else if (id == R.id.nav_salir){
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra(CLAVE_EMAIL, fbAuth.getCurrentUser().getEmail());
            startActivity(i);
            finish();
            fbAuth.signOut();
            mGoogleSignInClient.signOut();
            //overridePendingTransition(R.anim.rightin, R.anim.rightout);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirAdopta(View v){
        startActivity(new Intent(MainActivity.this, AdoptaListaActivity.class));
    }
    public void abrirAlertas(View v){
        startActivity(new Intent(MainActivity.this, AlertasListaActivity.class));
    }
}
