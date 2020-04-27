package com.dam.m21.petsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;

    EditText etNombre;
    EditText etContrasenia;
    EditText etEmail;
    String email;
    String password;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombreRegistro);
        etContrasenia = findViewById(R.id.etPasswordRegistro);
        etEmail = findViewById(R.id.etMailRegistro);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        if (fbUser != null) {
            etEmail.setText(fbUser.getEmail());
        }
    }

    public void registrar(View view) {
        if (validarDatos() == true) {
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fbUser = fbAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), R.string.toast_registro_correcto, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.rightin, R.anim.rightout);

                            } else {
                                Toast.makeText(RegistroActivity.this, getString(R.string.toast_user_no_registrado), Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, getString(R.string.toast_msj_no_datos), Toast.LENGTH_LONG).show();
            continuar = false;

        } else {
            continuar = true;
        }
        return continuar;
    }

    public void cerrarRegistro(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
