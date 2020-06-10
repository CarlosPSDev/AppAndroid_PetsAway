package com.dam.m21.petsaway.ajustes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dam.m21.petsaway.R;

public class ModEmailPass extends AppCompatActivity {

    static final String CLAVE_ANT="CLAVE_ANT";
    static final String CLAVE_NEW="CLAVE_NEW";
    static final String CLAVE_NEW_REP="CLAVE_NEW_REP";
    EditText et_ant,et_new,et_new_rep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_email_pass);
        et_ant=findViewById(R.id.etPassAnt);
        et_new=findViewById(R.id.etPassNew);
        et_new_rep=findViewById(R.id.etPassNewRep);

        String dato=getIntent().getStringExtra(AjustesActivity.CODE_DATO);
        String o_a=" ";
        if (dato.equals("contraseña")){o_a="a ";}
        else if (dato.equals("correo")){o_a="o ";}
        else{o_a=" ";}
        et_ant.setHint(String.format(getString(R.string.hint_pass_ant),dato));
        et_new.setHint(getString(R.string.hint_pass_new)+o_a+dato);
        et_new_rep.setHint(getString(R.string.hint_pass_new_rep)+o_a+dato);
        if(dato.equals("contraseña")||dato.equals("password")||dato.equals("passe")||dato.equals("пароль")){
            et_ant.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_candado, 0, 0, 0);
            et_new.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_candado, 0, 0, 0);
            et_new_rep.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_candado, 0, 0, 0);
        }
    }

    public void guardar(View view) {
        Intent intent= new Intent();
        String dato1 = et_ant.getText().toString();
        String dato2 = et_new.getText().toString();
        String dato3 = et_new_rep.getText().toString();
        intent.putExtra(CLAVE_ANT,dato1);
        intent.putExtra(CLAVE_NEW,dato2);
        intent.putExtra(CLAVE_NEW_REP,dato3);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelar(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
