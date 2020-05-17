package com.dam.m21.petsaway.alertas_lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity {
    EditText tipoAnimal,color,fecha,raza,desc;
    ImageButton btAddFoto;
    Button bt_buscado,bt_encontrado;
    String tipoAletra,sexo;
    private double latitud,longitud;
    RadioButton rbM,rbH;
    Map<String, Object> ubiPunto;

    private StorageReference msr;
    private FirebaseAuth fa;
    private FirebaseUser fu;
    Boolean fotoDesc;
    Uri miPath;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        tipoAnimal=findViewById(R.id.tipoAnimal);
        color=findViewById(R.id.color);
        fecha=findViewById(R.id.fecha);
        raza=findViewById(R.id.raza);
        desc=findViewById(R.id.desc);
        bt_buscado=findViewById(R.id.bt_buscado);
        bt_encontrado=findViewById(R.id.bt_encontrado);
        btAddFoto=findViewById(R.id.bt_add_foto);
        rbM=findViewById(R.id.rbM);
        rbH=findViewById(R.id.rbH);
        msr= FirebaseStorage.getInstance().getReference();
        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
        fotoDesc=false;

    }

    public void guardar(View view) {
        if(!tipoAnimal.getText().toString().isEmpty()&&!color.getText().toString().isEmpty()) {
        	if(rbM.isChecked()||rbH.isChecked()) {
		        if (rbM.isChecked()) sexo = "m";
		        if (rbH.isChecked()) sexo = "h";
		        latitud = getIntent().getExtras().getDouble("LAT", 1);
		        longitud = getIntent().getExtras().getDouble("LONG", 1);
		        Date fechaAct = new Date();
		        String fPush = fechaAct.toString();
		        ubiPunto = new HashMap<>();
		        ubiPunto.put("latitude", latitud);
		        ubiPunto.put("longitude", longitud);
		        ubiPunto.put("tipoAletra", tipoAletra);
		        ubiPunto.put("tipoAnimal", tipoAnimal.getText().toString());
		        ubiPunto.put("color", color.getText().toString());
		        ubiPunto.put("fecha", fecha.getText().toString());
		        ubiPunto.put("raza", raza.getText().toString());
		        ubiPunto.put("desc", desc.getText().toString());
		        ubiPunto.put("sexo", sexo);
		        ubiPunto.put("idFoto", miPath.getLastPathSegment());
		        ubiPunto.put("fPush", fPush);
		        ubiPunto.put("userPush", fu.getEmail());

		        StorageReference sr = msr.child("fotosAnimales").child(miPath.getLastPathSegment());
		        sr.putFile(miPath);
		        dbr = FirebaseDatabase.getInstance().getReference();
		        dbr.child("alertas").push().setValue(ubiPunto);
		        finish();
		        startActivity(new Intent(getApplicationContext(), AlertasMapaActivity.class));
	        }
        }else{
            Toast.makeText(getApplicationContext(),R.string.toast_faltanDatos, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==10){
            fotoDesc=true;
            miPath=data.getData();
            btAddFoto.setImageURI(miPath);

        }
    }

    public void addFoto(View view) {
        if(ContextCompat.checkSelfPermission(FormularioActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FormularioActivity.this,
                    new String[]{android.Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent,String.valueOf(R.string.msjBtDialogImage)),10);
    }

    public void encontrado(View view) {
        bt_buscado.setBackgroundResource(R.drawable.toolbar_style);
        bt_encontrado.setBackgroundResource(R.drawable.bt_tipo_de_alertas_habilitado);
        tipoAletra="encontrado";
    }
    public void buscado(View view) {
        bt_buscado.setBackgroundResource(R.drawable.bt_tipo_de_alertas_habilitado);
        bt_encontrado.setBackgroundResource(R.drawable.toolbar_style);
        tipoAletra="buscado";
    }
    public void calendario(View view) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.setCancelable(false);
        dpd.show();
    }
}
