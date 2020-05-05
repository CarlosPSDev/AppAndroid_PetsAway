package com.dam.m21.petsaway.perfil_usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.MainActivity;
import com.dam.m21.petsaway.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class ActAgregarMascota extends AppCompatActivity {
    private static final int REFERENCIA_FOTO = 2;
    DatabaseReference ref;
    StorageReference referenciaStor;
    ImageView imagenMascota;
    EditText etNombreM;
    Spinner spinEspecie;
    EditText etRazaM;
    EditText etColor;
    EditText etIdM;
    RadioButton rdbM, rdbH;
    EditText etDescripcion;
    TextView etFecha;
    EditText etTipoM;
    String especieSeleccionada;
    String urlM;

    ProgressDialog progres;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_agregar_mascota);
        urlM = "";
        etNombreM = findViewById(R.id.etNomMascA);
        etRazaM = findViewById(R.id.etRazaMascA);
        etColor = findViewById(R.id.etColorMascA);
        etIdM = findViewById(R.id.etIdMascA);
        rdbM = findViewById(R.id.rdbtMacho);
        rdbH = findViewById(R.id.rdbtHembra);
        etDescripcion = findViewById(R.id.etDescMascA);
        etFecha = findViewById(R.id.etFechaMascA);
        imagenMascota = findViewById(R.id.ivMascA);
        spinEspecie = findViewById(R.id.spinnerEspecies);
        etTipoM = findViewById(R.id.etTipoMascA);
        etFecha.setPaintFlags(View.INVISIBLE);
        progres = new ProgressDialog(this);

        userId = getIntent().getStringExtra("userId");
        referenciaStor = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference("MascotasUsers").child(userId);

        cargarSpinner();
    }

    public void guardarMascota(View view) {
        Toast t;
        String especieOtro = "";
        String sexoM = "";
        boolean especieOk = true;
        String nombreM = etNombreM.getText().toString();
        String razaM = etRazaM.getText().toString();
        String colorM = etColor.getText().toString();
        String idM = etIdM.getText().toString();
        if (rdbM.isChecked()) sexoM = "Macho"; if (rdbH.isChecked()) sexoM = "Hembra";
        String descripM = etDescripcion.getText().toString();
        String fechaM = etFecha.getText().toString();
        especieOtro = etTipoM.getText().toString();
        if (especieSeleccionada.equals("Otro") & especieOtro.isEmpty()) especieOk = false;

        if (!especieSeleccionada.equals("-*especie-") & !nombreM.isEmpty() & !fechaM.isEmpty() & especieOk){
            if (!especieOtro.isEmpty()) especieSeleccionada = especieOtro;
            PojoMascotas mascotaGuardar = new PojoMascotas(nombreM, especieSeleccionada, razaM, colorM, idM, descripM, sexoM, fechaM);

            HashMap<String, Object> mapaMascota = new HashMap<>();
            mapaMascota.put("nombre", nombreM);
            mapaMascota.put("especie", especieSeleccionada);
            mapaMascota.put("raza", razaM);
            mapaMascota.put("color", colorM);
            mapaMascota.put("identificacion", idM);
            mapaMascota.put("descrip", descripM);
            mapaMascota.put("sexo", sexoM);
            mapaMascota.put("fechaNac", fechaM);
            if (!urlM.isEmpty()) mapaMascota.put("urlImg", urlM);

            ref.child(userId + "-" + nombreM).updateChildren(mapaMascota);
            t = Toast.makeText(this, getString(R.string.toast_guardado_ok), Toast.LENGTH_SHORT); t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            cancelarG(view);
        } else {
            t = Toast.makeText(this, getString(R.string.toast_datos_vacios_m), Toast.LENGTH_SHORT); t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }

    public void cancelarG(View view) {
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }

    private void habilitarEditext(boolean habilitar){
        etTipoM.setEnabled(habilitar);
        if (habilitar) etTipoM.setVisibility(View.VISIBLE);
        else {
            etTipoM.setVisibility(View.INVISIBLE);
            etTipoM.setText("");
        }
    }

    public void mostrarCalendar(View view) {
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, //android.R.style.Theme_Holo_Light_Dialog_MinWidth, para cambiar formato
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etFecha.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, anio, mes, dia); //dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); para cambiar el color
        dpd.show();
    }

    public void subirImagen(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*"); //Si quisiéramos que sólo cogiera imágenes png o jpg lo especificaríamos
        startActivityForResult(i, REFERENCIA_FOTO);
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
            final StorageReference filePath = referenciaStor.child("fotosMascotasUser").child(userId)
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
                            urlM = uri.toString();

                            Glide.with(ActAgregarMascota.this).load(urlM)
                                    //.fitCenter().centerCrop()
                                    .into(imagenMascota);

                           /* HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("urlFotoMascota", urlM);  */

                        }
                    });

                    Toast.makeText(ActAgregarMascota.this, "Se subió exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActAgregarMascota.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Log.d("foto", "El activityResult no fue OK");
            Toast.makeText(ActAgregarMascota.this, "El activityResult no fue OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.especies_animales,
                android.R.layout.simple_spinner_item);
        spinEspecie.setAdapter(adapter);

        spinEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = (String) parent.getItemAtPosition(position);
                especieSeleccionada = seleccion;

                if (seleccion.equals("Otro")) habilitarEditext(true);
                else habilitarEditext(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

}
