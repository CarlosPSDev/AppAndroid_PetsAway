package com.dam.m21.petsaway.perfil_usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
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

public class PerfilUsuario extends AppCompatActivity  implements PerfilListener{
    static final int REFERENCIA_FOTO = 1;
    ProgressDialog progres;
    FirebaseDatabase db;
    DatabaseReference ref;
    StorageReference referenciaStor;
    //ArrayList<PojoUsuario> datosUsuario;
    String clave;
    PojoUser usuario;
    ValueEventListener vel;

    ImageView ivFotoUsuario;
    EditText etNombre;
    EditText etCiudad;
    TextView tvEmail;
    Group gFase1;
    Group gFase2;
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
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        progres = new ProgressDialog(this);
        referenciaStor = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance();
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
        clave = obtenerClave();






        ///////////////////modZori
        userid = fbUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("PETSAWAYusers").child(userid);
        ///////////////////modZori






        Log.d("Usuario", "El email logado es " + email);
        //ref = db.getReference().child("perfilesUsuarios").child(email);
        /*comentZori
        ref = db.getReference().child("perfilesUsuarios");
         */
        recuperarDatosFirebase();
        recuperarMascotasFirebase();
        //rellenarDatosonLoad();


        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

       /* listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));
        listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));*/
        /*listaMascotas.add(new PojoMascotas("Pompon", String.valueOf(R.drawable.slide_one_img)));
        listaMascotas.add(new PojoMascotas("Harry", String.valueOf(R.drawable.slide_two_img)));
        listaMascotas.add(new PojoMascotas("Luna", String.valueOf(R.drawable.slide_three_img)));*/

        adapter = new AdapterPetsProfile(listaMascotas);
        rv.setAdapter(adapter);
    }

    private void recuperarMascotasFirebase() {
    }

    private void rellenarDatosonLoad() {
        //ArrayList<PojoUsuario> objetoUser = recuperarDatosFirebase();

        ///////////////////modZori
        etNombre.setText(usuario.getNombre());
        ciudad = usuario.getCiudad();
        etCiudad.setText(ciudad);
        tvEmail.setText(email);
        uriImgGuardada = usuario.getUrlFotoUser();
        if (!uriImgGuardada.isEmpty()) Glide.with(PerfilUsuario.this).load(uriImgGuardada).into(ivFotoUsuario);//.fitCenter().centerCrop()
        else ivFotoUsuario.setImageResource(R.color.colorBlanco);

        ///////////////////modZori

        /*comentZori

        if (usuario != null){
            //usuario = datosUsuario.get(0);
            etNombre.setText(usuario.getNombre());
            ciudad = usuario.getCiudad();
            etCiudad.setText(ciudad);
            tvEmail.setText(email);
            uriImgGuardada = usuario.getUrlFotoUser();
            if (!uriImgGuardada.isEmpty()) Glide.with(PerfilUsuario.this).load(uriImgGuardada).into(ivFotoUsuario);//.fitCenter().centerCrop()
            else ivFotoUsuario.setImageResource(R.color.colorBlanco);

        } else {
            Toast.makeText(this, "No hay usuario en la base de datos, cargo datos de serie", Toast.LENGTH_SHORT).show();
            nombre = fbUser.getDisplayName();
            etNombre.setText(nombre);
            tvEmail.setText(email);
            ciudad = "";
            etCiudad.setText(ciudad);
            uriImgGuardada = "";
            ivFotoUsuario.setImageResource(R.color.colorBlanco);

            Toast.makeText(this, getString(R.string.toast_bienvenida), Toast.LENGTH_SHORT).show();*/
            /*user user = datasnapshot.getValue(User.Class);
            username.setText(user.getUsername);*/
      //comentZori }
        eliminarListener(); //Eliminamos el listener para que ya no siga actualizando los datos
    }
    private void recuperarDatosFirebase() {

        if (vel == null){
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //PojoUsuario user;
                    if (dataSnapshot.exists()){
                        ///////////////////modZori
                        usuario = dataSnapshot.getValue(PojoUser.class);
                        ///////////////////modZori



                      //comentZori  for (DataSnapshot datos : dataSnapshot.getChildren()){
                           // if (datos.getKey().equals(clave)) usuario = datos.getValue(PojoUser.class);
                            //Log.d("Usuario", "Nombre recopilado " + usuario.getNombre());
                            /*if (user.getEmail().equals(email)){
                                //datosUsuario.add(user);
                                usuario = user;
                                Log.d("Usuario", "Tamaño arraylist " + datosUsuario.size());
                            }comentZori*/
                      //comentZori  }
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

    /*private void recuperarDatosFirebase() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot datos : dataSnapshot.getChildren()){
                        PojoUsuario user = datos.getValue(PojoUsuario.class);
                        Log.d("Usuario", "Nombre recopilado " + user.getNombre());
                        datosUsuario.add(user);
                        Log.d("Usuario", "Tamaño arraylist " + datosUsuario.size());
                    }
                    rellenarDatosonLoad();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Usuario", "onCancelled: " + databaseError.getMessage());
            }
        });
    }*/


    public void modificarPerfil(View view) {
        uriImgNueva = uriImgGuardada;
        deshabilitarEditext(false);
    }

    public void guardarCambios(View view) {

        String nombreModif = etNombre.getText().toString();
        String ciudadModif = etCiudad.getText().toString();
        if (!nombreModif.isEmpty() & !ciudadModif.isEmpty()){
            if (!nombreModif.equals(nombre) | !ciudadModif.equals(ciudad) | !uriImgNueva.equals(uriImgGuardada)){
               //comentZori PojoUser usuarioModif = new PojoUser(nombreModif, email, ciudadModif, uriImgNueva);
                //Guardar en FireCloud

               // comentZoriref.child(clave).setValue(usuarioModif); //ref.child("perfilesUsuarios").setValue(usuarioModif); genera nodo

                ///////////////////modZori
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("nombre", nombreModif);
                hashMap.put("status", "offline");
                hashMap.put("ciudad", ciudadModif);
                hashMap.put("search", nombreModif.toLowerCase());
                ref.updateChildren(hashMap);
                ///////////////////modZori
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
        lanzarFragmento(new AniadirMascota());
    }

    private void lanzarFragmento(Fragment fragmenLanzar) {
        getSupportFragmentManager().beginTransaction().add(R.id.flContenedor, fragmenLanzar).commit(); //abreviado
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flContenedor, fragmenLanzar); //No hay que referenciar el fragment, aqui ya le das el id
        ft.addToBackStack(null); //Por si se da a retroceder q sepa q el fragmento tb esta
        ft.commit();*/
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
            final StorageReference filePath = referenciaStor.child("fotosPerfil").child(userid)
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

                            ///////////////////modZori
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("urlFotoUser", uriImgNueva);
                            ref.updateChildren(hashMap);
                            ///////////////////modZori

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
        } else{
            gFase1.setVisibility(View.INVISIBLE);
            gFase2.setVisibility(View.VISIBLE);
            etNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            rv.setVisibility(View.GONE);
        }
    }
    private void eliminarListener(){
        if(vel != null){ //quitar el listener
            ref.removeEventListener(vel);
            vel = null; //y lo anulamos
        }
    }
    private String obtenerClave(){
        int indice = email.indexOf('.');
        System.out.println(indice);
        String clave = email.substring(0, indice) + email.substring(indice+1);
        return clave;
    }

    @Override
    public void guardarMascota(PojoMascotas mascota) {
        //refMascotas.child(clave).setValue(mascota);
        Toast.makeText(this, getString(R.string.toast_guardado_ok), Toast.LENGTH_SHORT).show();
        recuperarDatosFirebase();
        //listaMascotas.add(mascota)

    }

    @Override
    public void cancelarOpcion() {
       getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.flContenedor)).commit();
       /*FragmentManager fragmentManager = getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.remove(new AniadirMascota());
       fragmentTransaction.commit();*/
    }
}
