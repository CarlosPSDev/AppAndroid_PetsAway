package com.dam.m21.petsaway.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.m21.petsaway.main.MainActivity;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.on_boarding.LanzadorOnBoard;
import com.dam.m21.petsaway.registro.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;

    EditText etContrasenia;
    EditText etEmail;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etMailLogin);
        etContrasenia = findViewById(R.id.etPasswordLogin);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        String mail = getIntent().getStringExtra("EMAIL");
        etEmail.setText(mail);

        if (fbUser != null) {
            etEmail.setText(fbUser.getEmail());
        }
    }

    public void contraseniaOlvidada(View view) {
    }

    public void accesoRegistro(View view) {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
        finish();
    }

    public void accesoAplicacion(View view) {
        if (validarDatos()) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //TODO: Animaciones
                                //Animation rotarIcono = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate_icon);
                                //ivLogo.startAnimation(rotarIcono);

                                fbUser = fbAuth.getCurrentUser();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        overridePendingTransition(R.anim.leftin, R.anim.leftout);
                                    }
                                }, 2000);



                            } else {
                                toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_error_acceso));
                            }
                        }
                    }
            );
        }
    }

    public void loginGmail(View view) {
    }

    public void loginFacebook(View view) {
    }

    public void abrirOnBoarding(View view) {
        Intent i = new Intent(this, LanzadorOnBoard.class);
        startActivity(i);
    }

    private boolean validarDatos() {
        email = etEmail.getText().toString().trim();
        password = etContrasenia.getText().toString().trim();
        boolean continuar;

        if (email.isEmpty() ||  password.isEmpty()) {
            toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_msj_no_datos));
            continuar = false;

        } else {
            continuar = true;
        }
        return continuar;
    }

    private void toastPersonalizado(String tit, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        TextView titulo = customToast.findViewById(R.id.tvTituloToast);
        titulo.setText(tit);
        texto.setText(text);
        Toast toast = new Toast(LoginActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }
}
