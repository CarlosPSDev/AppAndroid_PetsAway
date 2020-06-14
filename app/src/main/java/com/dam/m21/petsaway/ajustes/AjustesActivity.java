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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.m21.petsaway.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    String idUser;
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
                            toastPersonalizado(getString(R.string.msg_newEmail));
                        }else{
                            toastPersonalizado(getString(R.string.msg_newEmailDist));
                        }
                    }else{
                        toastPersonalizado(getString(R.string.msg_wrongEmail));
                    }

                } else {

                    fa.signInWithEmailAndPassword(fu.getEmail(), texto1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (texto2.equals(texto3)) {
                                    fu.updatePassword(texto3);
                                    toastPersonalizado(getString(R.string.msg_newPass));
                                }else{
                                    toastPersonalizado(getString(R.string.msg_newPassDist));
                                }
                            } else{
                                toastPersonalizado(getString(R.string.msg_wrongPass));
                                }
                            } });
                }

            } else if (resultCode == RESULT_CANCELED) {
                toastPersonalizado(getString(R.string.msg_cancel));
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
                    toastPersonalizado(getString(R.string.toast_foto_guardada));
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastPersonalizado(e.getMessage());
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
        intent.putExtra(CODE_DATO,getString(R.string.email));
        startActivityForResult(intent, REQUEST_CODE_EMAIL);

    }

    public void cambiarPass(View view) {

        Intent intent= new Intent(this,ModEmailPass.class);
        intent.putExtra(CODE_DATO,getString(R.string.contraseña));
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
    private void toastPersonalizado(String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        texto.setText(text);
        Toast toast = new Toast(AjustesActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }

}
