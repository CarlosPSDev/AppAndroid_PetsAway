package com.dam.m21.petsaway.alertas_adoptar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_lista.FormularioActivity;
import com.dam.m21.petsaway.alertas_lista.ListaAdapter;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.dam.m21.petsaway.perfil_usuario.PerfilUsuario;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FormularioAdoptaActivity extends AppCompatActivity {
	EditText tipoAnimal_adopta,color_adopta,edad_adopta,raza_adopta,desc_adopta,nik_adopta;
	ImageButton btAddFoto_adopta;
	String sexo_adopta;
	RadioButton rbM_adopta,rbH_adopta;
	private StorageReference msr;
	private FirebaseAuth fa;
	private FirebaseUser fu;
	Boolean fotoDesc;
	Uri miPath;
	DatabaseReference dbr;
	ArrayList<Fotos>listaFotos;
	RecyclerView rv_la;
	LinearLayoutManager llm;
	FotosAdapter la;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario_adopta);
		rv_la=findViewById(R.id.rv_fotos);
		nik_adopta=findViewById(R.id.nik_adopta);
		tipoAnimal_adopta=findViewById(R.id.tipoAnimal_adopta);
		color_adopta=findViewById(R.id.color_adopta);
		edad_adopta=findViewById(R.id.edad_adopta);
		raza_adopta=findViewById(R.id.raza_adopta);
		desc_adopta=findViewById(R.id.desc_adopta);
		btAddFoto_adopta=findViewById(R.id.bt_add_foto_adopta);
		rbM_adopta=findViewById(R.id.rbM_adopta);
		rbH_adopta=findViewById(R.id.rbH_adopta);
		msr= FirebaseStorage.getInstance().getReference();
		fa = FirebaseAuth.getInstance();
		fu = fa.getCurrentUser();
		fotoDesc=false;
		dbr = FirebaseDatabase.getInstance().getReference();
		listaFotos = new ArrayList<>();

        tipoAnimal_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        color_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        raza_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        desc_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        edad_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        desc_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        nik_adopta.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);

    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK && requestCode==10){
			fotoDesc=true;
			miPath=data.getData();
			//btAddFoto_adopta.setImageURI(miPath);
			StorageReference sr = msr.child("fotosAnimales").child(miPath.getLastPathSegment());
			sr.putFile(miPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
					Task<Uri> uriDireccFire = taskSnapshot.getStorage().getDownloadUrl();
					uriDireccFire.addOnSuccessListener(new OnSuccessListener<Uri>() {
						@Override
						public void onSuccess(Uri uri) {
							listaFotos.add(new Fotos(uri.toString()));
							llm = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
							rv_la.setLayoutManager(llm);
							la = new FotosAdapter(listaFotos);
							rv_la.setAdapter(la);
						}
					});
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
				}
			});

		}
	}

	public void addFoto(View view) {
		if(ContextCompat.checkSelfPermission(FormularioAdoptaActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(FormularioAdoptaActivity.this,
					new String[]{android.Manifest.permission.CAMERA,
							Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		}

		Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent.createChooser(intent,String.valueOf(R.string.msjBtDialogImage)),10);
	}

	public void calendario(View view) {
		Calendar c = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						edad_adopta.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dpd.setCancelable(false);
		dpd.show();
	}
	public void guardar(View view) {
		if(!nik_adopta.getText().toString().isEmpty()
				&&!tipoAnimal_adopta.getText().toString().isEmpty()
				&&!color_adopta.getText().toString().isEmpty()
				&&!edad_adopta.getText().toString().isEmpty()
				&&!raza_adopta.getText().toString().isEmpty()
				&&!desc_adopta.getText().toString().isEmpty()) {
			if(rbM_adopta.isChecked()||rbH_adopta.isChecked()) {
				if (rbM_adopta.isChecked()) sexo_adopta = "m";
				if (rbH_adopta.isChecked()) sexo_adopta = "h";
				Date fechaAct = new Date();
				String fPush = fechaAct.toString();
				HashMap adopta = new HashMap<>();
				adopta.put("tipoAnimal", tipoAnimal_adopta.getText().toString());
				adopta.put("color", color_adopta.getText().toString());
				adopta.put("edad", edad_adopta.getText().toString());
				adopta.put("raza", raza_adopta.getText().toString());
				adopta.put("desc", desc_adopta.getText().toString());
				adopta.put("sexo", sexo_adopta);
				adopta.put("fPush", fPush);
				adopta.put("userPush", fu.getEmail());
				adopta.put("idUserPush", fu.getUid());
				adopta.put("fotos", listaFotos);
				dbr.child("adoptar").push().updateChildren(adopta);
				finish();
				startActivity(new Intent(getApplicationContext(), AdoptaListaActivity.class));
			}
		}else{
            toastPersonalizado(getString(R.string.toast_faltanDatos));
			//Toast.makeText(getApplicationContext(),R.string.toast_faltanDatos, Toast.LENGTH_LONG).show();

		}
	}

    private void toastPersonalizado(String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast = inflater.inflate(R.layout.custom_toast, null);
        TextView texto = customToast.findViewById(R.id.tvTextoToast);
        texto.setText(text);
        Toast toast = new Toast(FormularioAdoptaActivity.this);
        toast.setGravity(Gravity.TOP, Gravity.CENTER , 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToast);
        toast.show();
    }
}
