package com.dam.m21.petsaway.alertas_adoptar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.chat.MessageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class DetalleAdoptaActivity extends AppCompatActivity {
		TextView tipoAnimal_adopta,color_adopta,edad_adopta,raza_adopta,desc_adopta,nik_adopta;
		ImageView foto_d, sexo_adopta_d;
		ArrayList<Fotos> listaFotos= new ArrayList<>();
		RecyclerView rv_la;
		AlertasList alad;
		LinearLayoutManager llm;
		FotosAdapter la;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_detalle_adopta);
			foto_d=findViewById(R.id.foto_d);
			rv_la=findViewById(R.id.rv_fotos_d);
			tipoAnimal_adopta=findViewById(R.id.tipoAnimal_adopta_d);
			color_adopta=findViewById(R.id.color_adopta_d);
			edad_adopta=findViewById(R.id.edad_adopta_d);
			raza_adopta=findViewById(R.id.raza_adopta_d);
			desc_adopta=findViewById(R.id.desc_adopta_d);
			sexo_adopta_d=findViewById(R.id.sexo_adopta_d);
			alad=getIntent().getParcelableExtra("ADOPTA");
			listaFotos=getIntent().getParcelableArrayListExtra("FOTOS");
			tipoAnimal_adopta.setText(alad.getTipoAnimal());
			color_adopta.setText(alad.getColor());
			edad_adopta.setText(alad.getEdad());
			raza_adopta.setText(alad.getRaza());
			desc_adopta.setText(alad.getDesc());
			if(alad.getSexo().equals("h")) {
				sexo_adopta_d.setImageResource(R.drawable.ic_hembra);
			}else{
				sexo_adopta_d.setImageResource(R.drawable.ic_macho);
			}
			Glide.with(getApplicationContext())
					.load(listaFotos.get(0).getUrl())
					.into(foto_d);
			llm = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
			rv_la.setLayoutManager(llm);
			la = new FotosAdapter(listaFotos);
			rv_la.setAdapter(la);
		}

	public void abrirChat(View view) {
		Intent intent = new Intent(getApplicationContext(),MessageActivity.class);
		intent.putExtra("userid", alad.getIdUserPush());
		startActivity(intent);
	}
}
