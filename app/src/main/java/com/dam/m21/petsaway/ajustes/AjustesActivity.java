package com.dam.m21.petsaway.ajustes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dam.m21.petsaway.login.LoginActivity;
import com.dam.m21.petsaway.model.PojoUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;
import com.dam.m21.petsaway.R;
import com.google.firebase.storage.UploadTask;

public class AjustesActivity extends AppCompatActivity {
    private FirebaseAuth fa;
    private FirebaseUser fu;
    Map<String, Object> userAjustes;
    private StorageReference msr;
    Uri miPath;
    DatabaseReference dbr;
    String tema,idUser;
    Button bt_tO,bt_tC;
    AlertDialog.Builder bd1;

    static final int REQUEST_CODE_PASS =1;
    static final int REQUEST_CODE_EMAIL =2;
    static final String CODE_DATO ="DATO";

    FirebaseAnalytics analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        fa = FirebaseAuth.getInstance();
        msr= FirebaseStorage.getInstance().getReference();
        fu = fa.getCurrentUser();

        idUser = fu.getUid();
        dbr = FirebaseDatabase.getInstance().getReference();
        bt_tO=findViewById(R.id.bt_tO);
        bt_tC=findViewById(R.id.bt_tC);
        tema="";
        datosUser();
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            analytics = FirebaseAnalytics.getInstance(AjustesActivity.this);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {}
                });
    }

    public void datosUser() {
        dbr.child("PETSAWAYusers").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PojoUser pUser = dataSnapshot.getValue(PojoUser.class);
                if(pUser!=null) {
                    String temaActual = pUser.getTema();
                    if(temaActual!=null) {
                        if (temaActual.equals("oscuro")) {
                            bt_tO.setBackgroundResource(R.drawable.bt_tema2_style_habilitado);
                        } else {
                            bt_tC.setBackgroundResource(R.drawable.bt_tema1_style_habilitado);
                        }
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
        dbr.child("PETSAWAYusers").child(idUser).updateChildren(userAjustes);
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

        if(requestCode== REQUEST_CODE_EMAIL||requestCode== REQUEST_CODE_PASS) {
            if (resultCode == RESULT_OK) {
                String texto1 = data.getStringExtra(ModEmailPass.CLAVE_ANT);
                final String texto2 = data.getStringExtra(ModEmailPass.CLAVE_NEW);
                final String texto3 = data.getStringExtra(ModEmailPass.CLAVE_NEW_REP);
                if (requestCode == REQUEST_CODE_EMAIL) {
                    if(texto1.equals(fu.getEmail())) {
                        if (texto2.equals(texto3)) {
                            fu.updateEmail(texto3);
                            Toast.makeText(getApplicationContext(), R.string.msg_newEmail, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.msg_newEmailDist, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.msg_wrongEmail, Toast.LENGTH_LONG).show();
                    }

                } else {

                    fa.signInWithEmailAndPassword(fu.getEmail(), texto1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (texto2.equals(texto3)) {
                                    fu.updatePassword(texto3);
                                    Toast.makeText(getApplicationContext(), R.string.msg_newPass, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), R.string.msg_newPassDist, Toast.LENGTH_LONG).show();
                                }
                            } else{
                                Toast.makeText(getApplicationContext(), R.string.msg_wrongPass, Toast.LENGTH_LONG).show();
                                }
                            } });
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.msg_cancel, Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode==RESULT_OK && requestCode==10){
            miPath=data.getData();
            StorageReference sr = msr.child("fondoChat").child(miPath.getLastPathSegment());
           // sr.putFile(miPath);

            sr.putFile(miPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriDireccFire = taskSnapshot.getStorage().getDownloadUrl();
                    uriDireccFire.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            userAjustes = new HashMap<>();
                            userAjustes.put("idFotoFondoChat", uri.toString());
                            dbr.child("PETSAWAYusers").child(idUser).updateChildren(userAjustes);
                        }
                    });

                    Toast.makeText(AjustesActivity.this, "Se subió exitosamente la foto", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AjustesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

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
        bd1= new androidx.appcompat.app.AlertDialog.Builder(AjustesActivity.this);
        bd1.setCancelable(false);
        bd1.setMessage(R.string.msjDialogborrarCuenta);
        bd1.setPositiveButton(R.string.msjBtDialogSI, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fu.delete();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        bd1.setNegativeButton(R.string.msjBtDialogNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        bd1.create().show();
    }

    public void cambiarCorreo(View view) {
        Intent intent= new Intent(this,ModEmailPass.class);
        intent.putExtra(CODE_DATO,"email");
        startActivityForResult(intent, REQUEST_CODE_EMAIL);

    }

    public void cambiarPass(View view) {

        Intent intent= new Intent(this,ModEmailPass.class);
        intent.putExtra(CODE_DATO,"contraseña");
        startActivityForResult(intent, REQUEST_CODE_PASS);

    }
    public static Uri buildDynamicLink() {
        Uri baseUrl = Uri.parse("https://petsaway.page.link/4Yif");
        String domain = "https://petsaway.page.link";

        DynamicLink link = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(baseUrl).setDomainUriPrefix(domain)
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.dam.m21.petsaway").build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.dam.m21.petsaway").build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle("Share this App").setDescription("blabla").build())
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder().setSource("AndroidApp").build())
                .buildDynamicLink();

        return link.getUri();
    }

    public void invitarAmigos(View view) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(buildDynamicLink()).buildShortDynamicLink()
            .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                @Override
                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                    if (task.isSuccessful()) {
                        Uri shortLink = task.getResult().getShortLink();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String msg = "visit my awesome website: " + shortLink.toString();
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, "Share Link"));
                    }
                }
            });
	}
}
