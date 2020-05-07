package com.dam.m21.petsaway.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.m21.petsaway.MainActivity;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.on_boarding.LanzadorOnBoard;
import com.dam.m21.petsaway.registro.RegistroActivity;
import com.dam.m21.petsaway.registro.reset_password.ResetPasswordActivity;
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

    static final String CLAVE_EMAIL = "MAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etMailLogin);
        etContrasenia = findViewById(R.id.etPasswordLogin);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        String mail = getIntent().getStringExtra("EMAIL");
        if (mail != null) {
            etEmail.setText(mail);
        }

        if (fbUser != null) {
            etEmail.setText(fbUser.getEmail());
        }
    }

    public void contraseniaOlvidada(View view) {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        startActivity(i);
    }

    public void accesoRegistro(View view) {
        email = etEmail.getText().toString().trim();
        Intent i = new Intent(this, RegistroActivity.class);
        if (!email.equals("")) {
            i.putExtra(CLAVE_EMAIL, email);
        }
        startActivity(i);
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
                                guardarDatosUserSP(fbUser.getDisplayName(), fbUser.getEmail(), fbUser.getUid()); //Carlos Guardamos los datos en shared

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*comprobarOnBoarding();*/ //Carlos---- Comprobamos si lanzar el onboarding o no

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
        texto.setText(text);
        Toast toast = new Toast(LoginActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }

    private void comprobarOnBoarding() { //Metodo para comprobar si lanzar el onBoarding o no. Llamarmlo desde las 3 opciones de login!
        SharedPreferences sharedPrefs = getSharedPreferences("ArchivoVeces", MODE_PRIVATE);
        int numVeces = sharedPrefs.getInt("vecesEjecutado", 0);

        if (numVeces > 0) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            numVeces++;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("vecesEjecutado", numVeces);
            editor.commit();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void guardarDatosUserSP(String nombre, String email, String userId){
        /*LLamar a este método cuando se valide el login, ya sea con firebase, google o facebook)!!*/
        SharedPreferences sharedPrefs = getSharedPreferences("ArchivoVeces", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("nombreUser", nombre);
        editor.putString("emailUser", email);
        editor.putString("idUser", userId);

        editor.commit();
    }

    private void recuperarDatosUserSP(){
        /*LLamar a este método desde cualquier activity para recuperar los datos dle usuario sin importar
         si se logó con firebase, google o facebook)!! Y borrarlo de este activity*/
        SharedPreferences sharedPrefs = getSharedPreferences("ArchivoVeces", MODE_PRIVATE);

        String nombre = sharedPrefs.getString("nombreUser", "Nombre no disponible");
        String email = sharedPrefs.getString("emailUser", "xxxxxx@...com");
        String id = sharedPrefs.getString("idUser", "0");
    }
}
