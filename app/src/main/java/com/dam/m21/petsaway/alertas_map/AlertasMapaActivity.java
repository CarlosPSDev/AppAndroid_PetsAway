package com.dam.m21.petsaway.alertas_map;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_lista.FormularioActivity;
import com.dam.m21.petsaway.chat.MessageActivity;
import com.dam.m21.petsaway.model.PojoUser;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AlertasMapaActivity extends AppCompatActivity implements OnMapReadyCallback { Button bt_encuentra,bt_busca;
ImageButton bt_add;
ImageView ImgM;
Button btAbrChat_mark;
LinearLayout dMP;
TextView tipoAnimalM,fechaEPAnimalM,colorAnimalM,userPushM;
    private GoogleMap mMap;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    static final String CLAVE_LAT = "LAT";
    static final String CLAVE_LONG = "LONG";
    String ta;
    String add;
    private FusedLocationProviderClient flpc;
    private StorageReference msr;
    private FirebaseAuth fa;
    private FirebaseUser fu;
    private int cont;
    DatabaseReference dbr;
    private LatLng miLoc;
    private Marker mark;
    String temaActual,idUser;
    private BottomSheetBehavior bsb;
    View bottomSheet;
    AlertasList pf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_mapa);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
        } else {
            Log.i("LOC", "with permission");
        }
        flpc = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bt_encuentra=findViewById(R.id.bt_encuentra);
        bt_busca=findViewById(R.id.bt_busca);
        bt_add=findViewById(R.id.bt_add);

        tipoAnimalM=findViewById(R.id.tipoAnimalM);
        fechaEPAnimalM=findViewById(R.id.fechaEPAnimalM);
        colorAnimalM=findViewById(R.id.colorAnimalM);
        userPushM=findViewById(R.id.userPushM);
        ImgM=findViewById(R.id.ImgM);
        btAbrChat_mark=findViewById(R.id.btAbrChat_mark);

        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
        idUser = fu.getUid();
        msr= FirebaseStorage.getInstance().getReference();
        dbr = FirebaseDatabase.getInstance().getReference();

        ta=getIntent().getStringExtra("TA");
        add=getIntent().getStringExtra("ADD");
        bottomSheet = findViewById(R.id.bottomJsoft);
        bsb = BottomSheetBehavior.from(bottomSheet);
        bsb.setPeekHeight(0);
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bt_add.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bt_add.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bt_add.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        datosUser();
    }

    private void goMiUbi() {
        flpc.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i("LOC", "onSuccess de location");
                if (location != null) {
                    double lon=location.getLongitude();
                    double lat=location.getLatitude();
                    miLoc = new LatLng(lat, lon);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
                }
            }
        });
    }

    public void datosUser() {
        dbr.child("PETSAWAYusers").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PojoUser pUser = dataSnapshot.getValue(PojoUser.class);
                if(pUser!=null) {
                    temaActual = pUser.getTema();
                    dMP = findViewById(R.id.dMP);
                    int idColor = R.color.colorPurpura;
                    if(temaActual!=null) {
                        View view=findViewById(android.R.id.content);
                        if (temaActual.equals("oscuro")) {
                            dMP.setBackgroundResource(idColor);

                            if (add!=null) {
                                Snackbar.make(view, R.string.toast_puntoMap, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                bt_add.setBackgroundResource(R.drawable.style_bt_add_habilitado_tema_oscuro);
                            }else{
                                bt_add.setBackgroundResource(R.drawable.style_bt_add_tema_oscuro);
                            }
                            bt_add.setImageResource(R.drawable.ic_add_tema_oscuro);
                        }else{
                            if (add!=null) {
                                Snackbar.make(view, R.string.toast_puntoMap, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                bt_add.setBackgroundResource(R.drawable.style_bt_add_habilitado);
                            }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ta != null) {
            if (ta.equals("encontrado")) {
                e();
            } else {
                b();
            }
        }else{e();}

        if (add!=null){
            goMiUbi();
            goFormBla();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                pf = (AlertasList) marker.getTag();
                if (pf.getTipoAnimal() != null) {
                    tipoAnimalM.setText(pf.getTipoAnimal());
                    fechaEPAnimalM.setText(pf.getFecha());
                    colorAnimalM.setText(pf.getColor());
                    userPushM.setText(pf.getUserPush());
                    String idF = pf.getIdFoto();
                    StorageReference sr = msr.child("fotosAnimales").child(idF);
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                                Glide.with(AlertasMapaActivity.this)
                                        .load(uri)
                                        .into(ImgM);
                            }
                    });
                    btAbrChat_mark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                            intent.putExtra("userid", pf.getIdUserPush());
                            startActivity(intent);
                        }
                    });
                    if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    return true;
                } else {
                    return false;
                }
            }
            });
    }

    public void goForm(View view) {
        goFormBla();
    }
    public void goFormBla() {
        cont=cont+1;
        if(cont%2!=0) {
            View view=findViewById(android.R.id.content);
            Snackbar.make(view, R.string.toast_puntoMap, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if(temaActual!=null) {
                if (temaActual.equals("oscuro")) {
                    bt_add.setBackgroundResource(R.drawable.style_bt_add_habilitado_tema_oscuro);
                } else {
                    bt_add.setBackgroundResource(R.drawable.style_bt_add_habilitado);
                }
            }
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(cont%2!=0) {
                        mark = mMap.addMarker(new MarkerOptions()
                                .position(latLng).title("Nueva posición")
                                .snippet("Lat: " + latLng.latitude + ", Long: " + latLng.longitude)
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN))
                        );
                        finish();
                        startActivity(new Intent(getApplicationContext(), FormularioActivity.class).putExtra(CLAVE_LAT, mark.getPosition().latitude).putExtra(CLAVE_LONG, mark.getPosition().longitude));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            });
        }else{
            if(temaActual!=null) {
                if (temaActual.equals("oscuro")) {
                    bt_add.setBackgroundResource(R.drawable.style_bt_add_tema_oscuro);
                } else {
                    bt_add.setBackgroundResource(R.drawable.style_bt_add);
                }
            }
        }
    }

    public void encuentra(View view) {
        e();
    }

    public void e(){
        bt_encuentra.setBackgroundResource(R.drawable.bt_tipo_de_alertas_habilitado);
        bt_busca.setBackgroundResource(R.drawable.toolbar_style);
        mMap.clear();
        dbr.child("alertas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
	                AlertasList pf = childDataSnapshot.getValue(AlertasList.class);
                    if(pf!=null) {
                    	String tA=pf.getTipoAletra();
                    	if(tA!=null) {
		                    if (tA.equals("encontrado")) {
			                    String tipoAnimal = pf.getTipoAnimal();
			                    double longitude = pf.getLongitude();
			                    double latitude = pf.getLatitude();
			                    miLoc = new LatLng(latitude, longitude);
			                    mMap.addMarker(new MarkerOptions().position(miLoc).title(tipoAnimal).icon(BitmapDescriptorFactory.defaultMarker(
					                    BitmapDescriptorFactory.HUE_GREEN))).setTag(pf);
			                    if (ta != null) {
				                    Double latAletra = getIntent().getExtras().getDouble("LAT_A", 1);
				                    Double lonAletra = getIntent().getExtras().getDouble("LON_A", 1);
				                    LatLng locA = new LatLng(latAletra, lonAletra);
				                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locA, 14));
			                    } else {
				                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
			                    }
		                    }
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
    public void busca(View view) {
        b();
    }

    public void b(){
        bt_busca.setBackgroundResource(R.drawable.bt_tipo_de_alertas_habilitado);
        bt_encuentra.setBackgroundResource(R.drawable.toolbar_style);
        mMap.clear();
        dbr.child("alertas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
	                AlertasList pf = childDataSnapshot.getValue(AlertasList.class);
	                if(pf!=null) {
		                String tA=pf.getTipoAletra();
		                if(tA!=null) {
			                if (tA.equals("buscado")) {
				                String tipoAnimal = pf.getTipoAnimal();
				                double longitude = pf.getLongitude();
				                double latitude = pf.getLatitude();
				                miLoc = new LatLng(latitude, longitude);
				                mMap.addMarker(new MarkerOptions().position(miLoc).title(tipoAnimal).icon(BitmapDescriptorFactory.defaultMarker(
						                BitmapDescriptorFactory.HUE_GREEN))).setTag(pf);

				                if (ta != null) {
					                Double latAletra = getIntent().getExtras().getDouble("LAT_A", 1);
					                Double lonAletra = getIntent().getExtras().getDouble("LON_A", 1);
					                LatLng locA = new LatLng(latAletra, lonAletra);
					                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locA, 14));
				                } else {
					                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
				                }
			                }
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
}
