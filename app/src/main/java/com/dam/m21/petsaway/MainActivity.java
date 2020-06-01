package com.dam.m21.petsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.acerca_de.AcercaDeActivity;
import com.dam.m21.petsaway.alertas_adoptar.AdoptaListaActivity;
import com.dam.m21.petsaway.alertas_lista.AlertasListaActivity;
import com.dam.m21.petsaway.ajustes.AjustesActivity;
import com.dam.m21.petsaway.chat.MainActivityChat;
import com.dam.m21.petsaway.login.LoginActivity;
import com.dam.m21.petsaway.aviso_legal.AvisoLegal;
import com.dam.m21.petsaway.model.PojoUser;
import com.dam.m21.petsaway.perfil_usuario.PerfilUsuario;
import com.dam.m21.petsaway.social_media.TwitterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Implementaciones Navigation Drawer !
    DrawerLayout drawer;
    ActionBarDrawerToggle toogle;
    NavigationView navController;

    private FirebaseAuth fbAuth;
    PojoUser usuario;
    String userid;
    String email;
    String uriImgGuardada;
    DatabaseReference ref;
    FirebaseUser fbUser;

    static final String CLAVE_EMAIL = "EMAIL";

    private GoogleSignInClient mGoogleSignInClient;

    ImageView imageViewUSerNav;
    TextView tvNameNav;
    TextView tvEmailNav;
    ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        userid = fbUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("PETSAWAYusers").child(userid);
        recuperarDatosFirebase();

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

        View navHeader = navController.getHeaderView(0);
        imageViewUSerNav = navHeader.findViewById(R.id.imageViewUser);
        tvNameNav = navHeader.findViewById(R.id.tvNameUserHeader);
        tvEmailNav = navHeader.findViewById(R.id.textViewEmailUser);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        } else if (id == R.id.nav_twitter){
            startActivity(new Intent(MainActivity.this, TwitterActivity.class));

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

    private void eliminarListener() {
        if (vel != null) {
            ref.removeEventListener(vel);
            vel = null;
        }
    }

    private void rellenarDatosNavHeader() {
        tvNameNav.setText(usuario.getNombre());
        email = fbUser.getEmail();
        tvEmailNav.setText(email);
        uriImgGuardada = usuario.getUrlFotoUser();
        if (uriImgGuardada != null)
            Glide.with(MainActivity.this).load(uriImgGuardada).into(imageViewUSerNav);
        else imageViewUSerNav.setImageResource(R.color.colorBlanco);

        eliminarListener();
    }

    private void recuperarDatosFirebase() {
        if (vel == null) {
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        usuario = dataSnapshot.getValue(PojoUser.class);
                        rellenarDatosNavHeader();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Usuario", "onCancelled: " + databaseError.getMessage());
                }
            };
            ref.addValueEventListener(vel);
        }
    }

    public void abrirAdopta(View v){
        startActivity(new Intent(MainActivity.this, AdoptaListaActivity.class));
    }
    public void abrirAlertas(View v){
        startActivity(new Intent(MainActivity.this, AlertasListaActivity.class));
    }
}
