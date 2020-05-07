package com.dam.m21.petsaway.perfil_usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.model.PojoUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;

public class PerfilUsuario extends AppCompatActivity {
    static final int REFERENCIA_FOTO = 1;
    ProgressDialog progres;
    //FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference refM;
    StorageReference referenciaStor;

    PojoUser usuario;
    ValueEventListener vel;

    ImageView ivFotoUsuario;
    EditText etNombre;
    EditText etCiudad;
    TextView tvEmail;
    Group gFase1;
    Group gFase2;
    String email;
    String uriImgGuardada;
    String uriImgNueva;

    ArrayList<PojoMascotas> listaMascotas;
    RecyclerView rv;
    AdapterPetsProfile adapter;

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        progres = new ProgressDialog(this);
        referenciaStor = FirebaseStorage.getInstance().getReference();
        //db = FirebaseDatabase.getInstance();
        //datosUsuario = new ArrayList<>();
        listaMascotas = new ArrayList<>();
        etNombre = findViewById(R.id.etValNomUsuario);
        etCiudad = findViewById(R.id.etValCiudadUsuario);
        gFase1 = findViewById(R.id.group);
        gFase2 = findViewById(R.id.group2);
        tvEmail = findViewById(R.id.tvValEmailUsuario);
        ivFotoUsuario = findViewById(R.id.ivImgUser);
        rv = findViewById(R.id.recyclerProfilPets);

        deshabilitarEditext(true);
        //TODO Recopilar el nombre del usuario según si el login ha sido con firebase, facebook o google

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        email = fbUser.getEmail();

        userId = fbUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("PETSAWAYusers").child(userId);

        refM = FirebaseDatabase.getInstance().getReference("MascotasUsers").child(userId);

        Log.d("Usuario", "El email logado es " + email);

        recuperarDatosFirebase();
        recuperarMascotasFirebase();
        //rellenarDatosonLoad();

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AdapterPetsProfile(listaMascotas, this);

        adapter.asignacionOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemIndex = rv.indexOfChild(v);
                PojoMascotas mascotaSelecc = listaMascotas.get(itemIndex);
                Intent i = new Intent(getApplicationContext(), ActModificarBorrar.class);
                i.putExtra("mascotaSelecc", mascotaSelecc);
                i.putExtra("userUid", userId);
                startActivity(i);
                PerfilUsuario.this.finish();
            }
        });

        rv.setAdapter(adapter);
    }

    private void recuperarMascotasFirebase() {
        refM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datos : dataSnapshot.getChildren()) {
                        PojoMascotas mascota = datos.getValue(PojoMascotas.class);
                        Log.d("mascotas", "Nombre recopilado " + mascota.getNombre());
                        listaMascotas.add(mascota);
                        Log.d("mascotas", "Tamaño arraylist " + listaMascotas.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("mascotas", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void rellenarDatosonLoad() {

        etNombre.setText(usuario.getNombre());
        if (usuario.getCiudad() == null) etCiudad.setText("Introduce tu ciudad");
        else etCiudad.setText(usuario.getCiudad());
        tvEmail.setText(email);
        uriImgGuardada = usuario.getUrlFotoUser();
        if (uriImgGuardada != null)
            Glide.with(PerfilUsuario.this).load(uriImgGuardada).into(ivFotoUsuario);//.fitCenter().centerCrop()
        else ivFotoUsuario.setImageResource(R.color.colorBlanco);

        eliminarListener();
    }

    private void recuperarDatosFirebase() {

        if (vel == null) {
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        usuario = dataSnapshot.getValue(PojoUser.class);
                        rellenarDatosonLoad();
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

    public void modificarPerfil(View view) {
        uriImgNueva = uriImgGuardada;
        deshabilitarEditext(false);
    }

    public void guardarCambios(View view) {

        String nombreModif = etNombre.getText().toString();
        String ciudadModif = etCiudad.getText().toString();
        if (!nombreModif.isEmpty() & !ciudadModif.isEmpty()) {
            if (!nombreModif.equals(usuario.getNombre()) | !ciudadModif.equals(usuario.getCiudad())) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("nombre", nombreModif);
                hashMap.put("status", "offline");
                hashMap.put("ciudad", ciudadModif);
                hashMap.put("search", nombreModif.toLowerCase());
                ref.updateChildren(hashMap);

            }
            deshabilitarEditext(true);
        } else {
            Toast.makeText(this, getString(R.string.toast_datos_vacios), Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiarFoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, REFERENCIA_FOTO);
    }

    public void agregarMascota(View view) {
        Intent i = new Intent(this, ActAgregarMascota.class);
        i.putExtra("userId", userId);
        startActivity(i);
        PerfilUsuario.this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REFERENCIA_FOTO & resultCode == RESULT_OK) {

            progres.setTitle("Subiendo foto...");
            progres.setMessage("Subiendo foto a Base de Datos");
            progres.setCancelable(false);
            progres.show();

            Uri uri = data.getData();
            final StorageReference filePath = referenciaStor.child("fotosPerfil").child(userId)
                    .child(uri.getLastPathSegment());

            Log.d("foto", "mostramos la uri en el movil " + uri.toString());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progres.dismiss();

                    Task<Uri> uriDireccFire = taskSnapshot.getStorage().getDownloadUrl();

                    uriDireccFire.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uriImgNueva = uri.toString();

                            Glide.with(PerfilUsuario.this).load(uriImgNueva)
                                    //.fitCenter().centerCrop()
                                    .into(ivFotoUsuario);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("urlFotoUser", uriImgNueva);
                            ref.updateChildren(hashMap);

                        }
                    });

                    Toast.makeText(PerfilUsuario.this, "Se subió exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(PerfilUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Log.d("foto", "El activityResult no fue OK");
            Toast.makeText(PerfilUsuario.this, "El activityResult no fue OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void deshabilitarEditext(Boolean hd) {
        etNombre.setFocusable(!hd);
        etNombre.setFocusableInTouchMode(!hd);
        etNombre.setCursorVisible(!hd);
        etNombre.setClickable(!hd);
        etCiudad.setFocusable(!hd);
        etCiudad.setFocusableInTouchMode(!hd);
        etCiudad.setCursorVisible(!hd);
        etCiudad.setClickable(!hd);
        tvEmail.setEnabled(hd);
        gFase1.setEnabled(!hd);
        gFase2.setEnabled(!hd);
        rv.setEnabled(hd);
        if (hd) {
            gFase1.setVisibility(View.VISIBLE);
            gFase2.setVisibility(View.INVISIBLE);
            etNombre.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.VISIBLE);
        } else {
            gFase1.setVisibility(View.INVISIBLE);
            gFase2.setVisibility(View.VISIBLE);
            etNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.GONE);
        }
    }

    private void eliminarListener() {
        if (vel != null) {
            ref.removeEventListener(vel);
            vel = null;
        }
    }
}
