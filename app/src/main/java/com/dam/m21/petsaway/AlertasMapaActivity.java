package com.dam.m21.petsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.dam.m21.petsaway.pojos.PojoFormulario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlertasMapaActivity extends AppCompatActivity implements OnMapReadyCallback {
Button bt_encuentra,bt_busca;
ImageButton bt_add;

    private GoogleMap mMap;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    static final String CLAVE_LAT = "LAT";
    static final String CLAVE_LONG = "LONG";

    private int cont;
    DatabaseReference dbr;
    private LatLng miLoc;
    private Marker mark;

    Toolbar toolbar;

    private BottomSheetBehavior bsb;
    View bottomSheet;
    PojoFormulario pf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_mapa);
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
        } else {
            Log.i("LOC", "with permission");
        }*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bt_encuentra=findViewById(R.id.bt_encuentra);
        bt_busca=findViewById(R.id.bt_busca);
        bt_add=findViewById(R.id.bt_add);

        dbr = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        e();
    }

    public void goForm(View view) {
        cont=cont+1;
        if(cont%2!=0) {
            bt_add.setBackgroundResource(R.drawable.style_bt_add_habilitado);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(cont%2!=0) {
                        mark = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Nueva posición")
                                .snippet("Lat: " + latLng.latitude + ", Long: " + latLng.longitude)
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN))
                        );
                        startActivity(new Intent(getApplicationContext(), FormularioActivity.class).putExtra(CLAVE_LAT, mark.getPosition().latitude).putExtra(CLAVE_LONG, mark.getPosition().longitude));

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            });
        }else{
            bt_add.setBackgroundResource(R.drawable.style_bt_add);
        }
    }

    public void encuentra(View view) {
        e();
    }

    public void e(){
        bt_encuentra.setBackgroundResource(R.drawable.bt_tipo_de_alertas_habilitado);
        bt_busca.setBackgroundResource(R.drawable.toolbar_style);
        mMap.clear();
        dbr.child("alertas").child("encontrados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    PojoFormulario pf=childDataSnapshot.getValue(PojoFormulario.class);
                    double longitude=pf.getLongitude();
                    double latitude=pf.getLatitude();
                    String tipoAnimal=pf.getTipoAnimal();
                    miLoc=new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(miLoc).title(tipoAnimal).icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN))).setTag(pf);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
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
        dbr.child("alertas").child("buscados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    PojoFormulario pf=childDataSnapshot.getValue(PojoFormulario.class);
                    double longitude=pf.getLongitude();
                    double latitude=pf.getLatitude();
                    String tipoAnimal=pf.getTipoAnimal();
                    miLoc=new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(miLoc).title(tipoAnimal).icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN))).setTag(pf);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}
