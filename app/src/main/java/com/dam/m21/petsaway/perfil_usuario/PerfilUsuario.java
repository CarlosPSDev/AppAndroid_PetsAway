package com.dam.m21.petsaway.perfil_usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    DatabaseReference ref;
    DatabaseReference refM;
    StorageReference referenciaStor;

    PojoUser usuario;
    ValueEventListener vel;

    ImageView ivFotoUsuario;
    ImageView ivGuardarCambios;
    ImageView ivIconoCamara;
    EditText etNombre;
    EditText etCiudad;
    TextView tvEmail;
    //LinearLayout llMascotas;
    Button llMascotas;

    String email;
    String uriImgGuardada;
    String uriImgNueva;
    String userId;

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

        listaMascotas = new ArrayList<>();
        etNombre = findViewById(R.id.etValNomUsuario);
        etCiudad = findViewById(R.id.etValCiudadUsuario);
        tvEmail = findViewById(R.id.tvValEmailUsuario);
        ivFotoUsuario = findViewById(R.id.ivImgUser);
        ivGuardarCambios = findViewById(R.id.btnGuardarCambios);
        ivIconoCamara = findViewById(R.id.ivIconoCamara);
        //llMascotas = findViewById(R.id.llMascotas);
        llMascotas = findViewById(R.id.btnAddMasc);

        rv = findViewById(R.id.recyclerProfilPets);

        deshabilitarEditext(true);
        //TODO Recopilar el nombre del usuario según si el login ha sido con firebase, facebook o google

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        email = fbUser.getEmail();
        recuperarDatosUserSP();
        userId = fbUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("PETSAWAYusers").child(userId);
        refM = FirebaseDatabase.getInstance().getReference("MascotasUsers").child(userId);

        Log.d("Usuario", "El email logado es " + email);

        recuperarDatosFirebase();
        recuperarMascotasFirebase();

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
    }

    private void recuperarMascotasFirebase() {
        refM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datos : dataSnapshot.getChildren()) {
                        PojoMascotas mascota = datos.getValue(PojoMascotas.class);
                        listaMascotas.add(mascota);
                    }
                    rv.setAdapter(adapter);
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
            toastPersonalizado(getString(R.string.toast_cambios_ok));
            deshabilitarEditext(true);
        } else {
            toastPersonalizado(getString(R.string.toast_datos_vacios));
        }
        // Estas líneas esconden el teclado al guardar los cambios
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etCiudad.getWindowToken(), 0);
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
                    toastPersonalizado(getString(R.string.toast_foto_guardada));

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(PerfilUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Log.d("foto", "El activityResult no fue OK");
            //Toast.makeText(PerfilUsuario.this, "El activityResult no fue OK", Toast.LENGTH_SHORT).show();
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
        ivGuardarCambios.setEnabled(!hd);
        ivIconoCamara.setClickable(hd);
        rv.setEnabled(hd);
        rv.setVisibility(View.VISIBLE);
        llMascotas.setEnabled(hd);

        if (hd) {
            etNombre.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            ivGuardarCambios.setVisibility(View.INVISIBLE);

        } else {
            etNombre.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            etCiudad.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            ivGuardarCambios.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            llMascotas.setEnabled(hd);
        }
    }

    private void eliminarListener() {
        if (vel != null) {
            ref.removeEventListener(vel);
            vel = null;
        }
    }
    private void recuperarDatosUserSP(){
        /*LLamar a este método desde cualquier activity para recuperar los datos dle usuario sin importar
         si se logó con firebase, google o facebook)!!*/
        SharedPreferences sharedPrefs = getSharedPreferences("ArchivoVeces", MODE_PRIVATE);

        String nombre = sharedPrefs.getString("nombreUser", "Nombre no disponible");
        String email = sharedPrefs.getString("emailUser", "xxxxxx@...com");
        String id = sharedPrefs.getString("idUser", "0");
        Log.d("RecuperarDatos", "nombre: " + nombre);
        Log.d("RecuperarDatos", "email: " + email);
        Log.d("RecuperarDatos", "id: " + id);
    }

    private void toastPersonalizado(String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        texto.setText(text);
        Toast toast = new Toast(PerfilUsuario.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }
}
