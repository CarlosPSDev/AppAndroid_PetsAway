package com.dam.m21.petsaway.registro.reset_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.login.LoginActivity;
import com.dam.m21.petsaway.registro.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;

    FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        send_email = findViewById(R.id.etMailRecuperarPassword);
        btn_reset = findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();
        fbUser = firebaseAuth.getCurrentUser();

        if (fbUser != null) {
            send_email.setText(fbUser.getEmail());
        }

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = send_email.getText().toString().trim();

                if (email.equals("")){
                    toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_recuperar_password));
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_revisa_email));
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                toastPersonalizado(getString(R.string.app_name), getString(R.string.toast_error_envio_correo));
                            }
                        }
                    });
                }
            }
        });
    }

    private void toastPersonalizado(String tit, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        texto.setText(text);
        Toast toast = new Toast(ResetPasswordActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }
}
