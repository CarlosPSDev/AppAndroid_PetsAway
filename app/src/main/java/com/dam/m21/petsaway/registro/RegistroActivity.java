package com.dam.m21.petsaway.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;

    EditText etNombre;
    EditText etContrasenia;
    EditText etEmail;
    String email;
    String password;
    String user;

    static final String CLAVE_EMAIL = "EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombreRegistro);
        etContrasenia = findViewById(R.id.etPasswordRegistro);
        etEmail = findViewById(R.id.etMailRegistro);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        String email = getIntent().getStringExtra("MAIL");
        if (email != null) {
            etEmail.setText(email);
        }

        if (fbUser != null) {
            etEmail.setText(fbUser.getEmail());
        }
    }

    public void registrar(View view) {
        if (validarDatos()) {
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fbUser = fbAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user).build();

                                fbUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("almacenarN", "Nombre guardado: " + user);
                                    }
                                });

                                toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_registro_correcto));
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.rightin, R.anim.rightout);

                            } else {
                                toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_user_no_registrado));
                            }
                        }
                    });
        }
    }

    private boolean validarDatos() {
        user = etNombre.getText().toString().trim();
        password = etContrasenia.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        boolean continuar;

        if (user.isEmpty() || email.isEmpty() || password.isEmpty()) {
            toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_msj_no_datos));
            continuar = false;

        } else {
            continuar = true;
        }
        return continuar;
    }

    public void cerrarRegistro(View view) {
        email = etEmail.getText().toString().trim();
        Intent i = new Intent(this, LoginActivity.class);
        if (!email.equals("")) {
            i.putExtra(CLAVE_EMAIL, email);
        }
        startActivity(i);
        finish();
    }


    private void toastPersonalizado(String tit, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        texto.setText(text);
        Toast toast = new Toast(RegistroActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }
}
