package com.dam.m21.petsaway.perfil_usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
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

public class PerfilUsuario extends AppCompatActivity {
    static final int REFERENCIA_FOTO = 1;
    ProgressDialog progres;
    FirebaseDatabase db;
    DatabaseReference ref;
    StorageReference referenciaStor;
    ArrayList<PojoUsuario> objetoUser;
    PojoUsuario usuario;

    ImageView ivFotoUsuario;
    EditText etNombre;
    EditText etCiudad;
    TextView tvEmail;
    Button btnEditarP;
    Button btnAgregarPet;
    Button btnGuardarCambios;
    Button btnCambiarFoto;
    String email;
    String ciudad;
    String nombre;
    String uriImgGuardada;
    String uriImgNueva;

    ArrayList<PojoMascotas> listaMascotas;
    RecyclerView rv;
    AdapterPetsProfile adapter;
    
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        progres = new ProgressDialog(this);
        referenciaStor = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance();


        etNombre = findViewById(R.id.etValNomUsuario);
        etCiudad = findViewById(R.id.etValCiudadUsuario);
        btnEditarP = findViewById(R.id.btnModifPerfil);
        btnAgregarPet = findViewById(R.id.btnAgregarMasc);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        tvEmail = findViewById(R.id.tvValEmailUsuario);
        ivFotoUsuario = findViewById(R.id.ivImgUser);
        rv = findViewById(R.id.recyclerProfilPets);

        deshabilitarEditext(true);
        //TODO Recopilar el nombre del usuario según si el login ha sido con firebase, facebook o google

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        email = fbUser.getEmail();
        ref = db.getReference().child("perfilesUsuario").child(email);
        rellenarDatosonLoad();


        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        listaMascotas = new ArrayList<>();
        listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));
        listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));
        /*listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));*/

        adapter = new AdapterPetsProfile(listaMascotas);
        rv.setAdapter(adapter);
    }

    private void rellenarDatosonLoad() {
        objetoUser = recuperarDatosFirebase();
        if (!objetoUser.isEmpty()){
            usuario = objetoUser.get(0);

            etNombre.setText(usuario.getNombre());
            etCiudad.setText(usuario.getCiudad());
            uriImgGuardada = usuario.getUrl();
            Glide.with(PerfilUsuario.this).load(uriImgGuardada)
                    //.fitCenter().centerCrop()
                    .into(ivFotoUsuario);
        } else {
            nombre = fbUser.getDisplayName();
            etNombre.setText(nombre);
            tvEmail.setText(email);
            etCiudad.setText("");
            uriImgGuardada = "";
            ivFotoUsuario.setImageResource(R.color.colorBlanco);

            Toast.makeText(this, getString(R.string.toast_bienvenida), Toast.LENGTH_SHORT).show();
            /*user user = datasnapshot.getValue(User.Class);
            username.setText(user.getUsername);*/
        }
    }

    private ArrayList<PojoUsuario> recuperarDatosFirebase() {
        final ArrayList<PojoUsuario> datosUsuario = new ArrayList<PojoUsuario>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot datos : dataSnapshot.getChildren()){
                        PojoUsuario user = datos.getValue(PojoUsuario.class);
                        Log.d("Usuario", "Nombre recopilado " + user.getNombre());
                        datosUsuario.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Usuario", "onCancelled: " + databaseError.getMessage());
            }
        });
        Log.d("Usuario", "Tamaño arraylist " + datosUsuario.size());
        return datosUsuario;
    }


    public void modificarPerfil(View view) {
        uriImgNueva = uriImgGuardada;
        deshabilitarEditext(false);
    }

    public void guardarCambios(View view) {
        String nombreModif = etNombre.getText().toString();
        String ciudadModif = etCiudad.getText().toString();
        if (!nombre.isEmpty() & !ciudad.isEmpty()){
            if (!nombreModif.equals(nombre) | !ciudadModif.equals(ciudad) | uriImgNueva.equals(uriImgGuardada)){
                PojoUsuario usuarioModif = new PojoUsuario(nombreModif, email, ciudadModif, uriImgNueva);
                //Guardar en FireCloud
            }


            deshabilitarEditext(true);
        } else {
            Toast.makeText(this, getString(R.string.toast_datos_vacios), Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiarFoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*"); //Si quisiéramos que sólo cogiera imágenes png o jpg lo especificaríamos
        startActivityForResult(i, REFERENCIA_FOTO);
    }

    public void agregarMascota(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REFERENCIA_FOTO & resultCode == RESULT_OK){

            progres.setTitle("Subiendo foto...");
            progres.setMessage("Subiendo foto a Base de Datos");
            progres.setCancelable(false);
            progres.show();

            Uri uri = data.getData();
            final StorageReference filePath = referenciaStor.child("fotos de perfil").child(email)
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
                            //Log.d("foto", "uri en Storage " + uri.toString());
                            Glide.with(PerfilUsuario.this).load(uriImgNueva)
                                    //.fitCenter().centerCrop()
                                    .into(ivFotoUsuario);

                        }
                    });

                    Toast.makeText(PerfilUsuario.this, "Se subió exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.d("foto", e.getMessage());
                            Toast.makeText(PerfilUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("foto", "El activityResult no fue OK");
            Toast.makeText(PerfilUsuario.this, "El activityResult no fue OK", Toast.LENGTH_SHORT).show();
        }
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
        btnCambiarFoto.setEnabled(!hd);
        rv.setEnabled(hd);
        if (hd) {
            btnEditarP.setVisibility(View.VISIBLE);
            btnAgregarPet.setVisibility(View.VISIBLE);
            btnGuardarCambios.setVisibility(View.INVISIBLE);
            btnCambiarFoto.setVisibility(View.INVISIBLE);
            etNombre.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.VISIBLE);
        } else{
            btnEditarP.setVisibility(View.INVISIBLE);
            btnAgregarPet.setVisibility(View.INVISIBLE);
            btnGuardarCambios.setVisibility(View.VISIBLE);
            btnCambiarFoto.setVisibility(View.VISIBLE);
            etNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.GONE);
        }
    }
}
