package com.dam.m21.petsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dam.m21.petsaway.pojos.PojoUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AjustesActivity extends AppCompatActivity {
    private FirebaseAuth fa;
    private FirebaseUser fu;
    Map<String, Object> userAjustes;
    EditText nuevoEmail,nuevoPass;
    private StorageReference msr;
    Uri miPath;
    DatabaseReference dbr;
    String tema;
    Button bt_tO,bt_tC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        fa = FirebaseAuth.getInstance();
        msr= FirebaseStorage.getInstance().getReference();
        fu = fa.getCurrentUser();
        dbr = FirebaseDatabase.getInstance().getReference();
        bt_tO=findViewById(R.id.bt_tO);
        bt_tC=findViewById(R.id.bt_tC);
        nuevoEmail=findViewById(R.id.nuevoEmail);
        nuevoPass=findViewById(R.id.nuevoPass);
        tema="";
        datosUser();
    }

    public void datosUser() {
        dbr.child("usuarios").child(fu.getEmail().substring(0, fu.getEmail().indexOf("."))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PojoUser pUser = dataSnapshot.getValue(PojoUser.class);
                if(pUser!=null) {
                    String temaActual = pUser.getTema();
                    if (temaActual.equals("oscuro")) {
                        bt_tO.setBackgroundResource(R.drawable.bt_tema2_style_habilitado);
                    } else {
                        bt_tC.setBackgroundResource(R.drawable.bt_tema1_style_habilitado);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void modTema() {
        userAjustes = new HashMap<>();
        userAjustes.put("tema", tema);
        dbr.child("usuarios").child(fu.getEmail().substring(0, fu.getEmail().indexOf("."))).setValue(userAjustes);
    }

    public void temaClaro(View view) {
        bt_tO.setBackgroundResource(R.drawable.bt_tema2_style);
        bt_tC.setBackgroundResource(R.drawable.bt_tema1_style_habilitado);
        tema="claro";
        modTema();
    }

    public void temaOscuro(View view) {
        bt_tO.setBackgroundResource(R.drawable.bt_tema2_style_habilitado);
        bt_tC.setBackgroundResource(R.drawable.bt_tema1_style);
        tema="oscuro";
        modTema();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==10){
            miPath=data.getData();
            userAjustes = new HashMap<>();
            userAjustes.put("idFotoFondoChat", miPath.getLastPathSegment());
            StorageReference sr = msr.child("fondoChat").child(miPath.getLastPathSegment());
            sr.putFile(miPath);
            dbr.child("usuariosFondoChat").child(fu.getEmail().substring(0, fu.getEmail().indexOf("."))).setValue(userAjustes);

        }
    }
    public void modFondoDelChat(View view) {
        if(ContextCompat.checkSelfPermission(AjustesActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjustesActivity.this,
                    new String[]{android.Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent,String.valueOf(R.string.msjBtDialogImage)),10);
    }

    public void borrarMiCuenta(View view) {
        fu.delete();
    }

    public void cambiarCorreo(View view) {
        fu.updateEmail(nuevoEmail.getText().toString());
    }

    public void cambiarPass(View view) {
        fu.updatePassword(nuevoPass.getText().toString());
    }
}
