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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.acerca_de.AcercaDeActivity;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Implementaciones Navigation Drawer !
    DrawerLayout drawer;
    ActionBarDrawerToggle toogle;
    NavigationView navController;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth fbAuth;
    PojoUser usuario;
    String userid;
    String email;
    String uriImgGuardada;
    DatabaseReference ref;
    FirebaseUser fbUser;

    static final String CLAVE_EMAIL = "EMAIL";
    static String EMAIL_GOOGLE = "";
    TextView tvInfoEmail;
    ImageView imageViewUSerNav;
    TextView tvNameNav;
    TextView tvEmailNav;
    ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        userid = fbUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("PETSAWAYusers").child(userid);
        recuperarDatosFirebase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        drawer.addDrawerListener(toogle);
        toogle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
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

        /**GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            EMAIL_GOOGLE = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            tvInfoEmail.setText(EMAIL_GOOGLE + personName + personId);

            Glide.with(MainActivity.this).load(personPhoto).into(imageViewPrueba);

        }**/
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
            startActivity(new Intent(MainActivity.this, AlertasMapaActivity.class));

        } else if (id == R.id.nav_adoptar){
            startActivity(new Intent(MainActivity.this, AlertasListaActivity.class));

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
            i.putExtra(CLAVE_EMAIL, EMAIL_GOOGLE);
            startActivity(i);
            finish();
            fbAuth.signOut();
            googleSignInClient.signOut();
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


}
