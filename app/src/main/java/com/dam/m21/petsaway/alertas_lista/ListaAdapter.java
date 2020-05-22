package com.dam.m21.petsaway.alertas_lista;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.dam.m21.petsaway.chat.MessageActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListViewHolder>
        implements View.OnClickListener {

private View.OnClickListener ocListener;
private ArrayList<AlertasList> datos;
    static final String CLAVE_LAT = "LAT_A";
    static final String CLAVE_LON = "LON_A";
    static final String CLAVE_TA = "TA";
    AlertDialog.Builder ad;

    public ListaAdapter(ArrayList<AlertasList> datos) {
        this.datos = datos;
        }

public static class ListViewHolder extends RecyclerView.ViewHolder{
    private ImageView image_elap;
    private TextView nombre_elap;
    private TextView fecha_elap;
    LinearLayout ll_elap;
    ImageButton bt_goMap,bt_borrarAlerta;
    ImageView sexo_elap,tipoAl_elap;

    public ListViewHolder(View v) {
        super(v);
        image_elap = v.findViewById(R.id.image_elap);
        nombre_elap = v.findViewById(R.id.nombre_elap);
        fecha_elap = v.findViewById(R.id.fecha_elap);
        bt_goMap=v.findViewById(R.id.bt_goMap);
        sexo_elap=v.findViewById(R.id.sexo_elap);
        ll_elap=v.findViewById(R.id.ll_elap);
        tipoAl_elap=v.findViewById(R.id.tipoAl_elap);
        bt_borrarAlerta=v.findViewById(R.id.bt_borrarAlerta);
    }

    public void bindAList(AlertasList il){
        StorageReference msr= FirebaseStorage.getInstance().getReference();
        StorageReference sr = msr.child("fotosAnimales").child(il.getIdFoto());
        sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(itemView.getContext())
                        .load(uri)
                        .into(image_elap);
            }
        });
        if(il.getTipoAletra()!=null) {
            if (il.getTipoAletra().equals("buscado")) {
                ll_elap.setBackgroundResource(R.drawable.style_detalle_alerta);
                tipoAl_elap.setImageResource(R.drawable.ic_bus);
            } else {
                ll_elap.setBackgroundResource(R.drawable.style_detalle_alerta_enc);
                tipoAl_elap.setImageResource(R.drawable.ic_enc);
            }
        }
        if (il.getSexo().equals("m")){
            sexo_elap.setImageResource(R.drawable.ic_macho);
        }else{
            sexo_elap.setImageResource(R.drawable.ic_hembra);
        }
        if(il.getUserPush().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            bt_borrarAlerta.setVisibility(View.VISIBLE);
        }else {
            bt_borrarAlerta.setVisibility(View.GONE);
        }
        nombre_elap.setText(il.getTipoAnimal());
        fecha_elap.setText(il.getFecha());
    }
}

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_alertas_perdidos, parent, false);
        v.setOnClickListener(this);
        ListViewHolder avh = new ListViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        holder.bindAList(datos.get(position));
        holder.bt_goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), AlertasMapaActivity.class);
                Double latitude=datos.get(position).getLatitude();
                Double longitude=datos.get(position).getLongitude();
                intent.putExtra(CLAVE_LAT, latitude);
                intent.putExtra(CLAVE_LON, longitude);
                intent.putExtra(CLAVE_TA, datos.get(position).getTipoAletra());
                v.getContext().startActivity(intent);
            }
        });
        holder.image_elap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater LI=LayoutInflater.from(holder.itemView.getContext());
                final View vista=LI.inflate(R.layout.detalle_alerta,null);
                ad = new AlertDialog.Builder(holder.itemView.getContext());
                ad.setCancelable(false);
                final AlertDialog adC=ad.create();

                Button bt_close_dialog=vista.findViewById(R.id.bt_close_dialog);
                TextView detalle_tipoAletra=vista.findViewById(R.id.detalle_tipoAletra);
                TextView detalle_tipoAnimal=vista.findViewById(R.id.detalle_tipoAnimal);
                TextView detalle_color=vista.findViewById(R.id.detalle_color);
                TextView detalle_fecha=vista.findViewById(R.id.detalle_fecha);
                TextView detalle_raza=vista.findViewById(R.id.detalle_raza);
                TextView detalle_desc=vista.findViewById(R.id.detalle_desc);
                TextView detalle_fPush=vista.findViewById(R.id.detalle_fPush);
                TextView detalle_userPush=vista.findViewById(R.id.detalle_userPush);
                Button btAbrChat=vista.findViewById(R.id.btAbrChat);

                detalle_tipoAletra.setText(datos.get(position).getTipoAletra());
                detalle_tipoAnimal.setText(datos.get(position).getTipoAnimal());
                detalle_color.setText(datos.get(position).getColor());
                detalle_fecha.setText(datos.get(position).getFecha());
                detalle_raza.setText(datos.get(position).getRaza());
                detalle_desc.setText(datos.get(position).getDesc());
                detalle_fPush.setText(datos.get(position).getfPush());
                detalle_userPush.setText(datos.get(position).getUserPush());
                btAbrChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MessageActivity.class);
                        AlertasList alertaPos=datos.get(position);
                        intent.putExtra("userid", alertaPos.getIdUserPush());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
                bt_close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adC.cancel();
                    }
                });
                if(vista.getParent() != null) {
                    ((ViewGroup)vista.getParent()).removeView(vista);
                }
                adC.setView(vista);
                adC.show();
            }
        });
        holder.bt_borrarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseDatabase.getInstance().getReference().child("alertas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                            AlertasList alertaPos=datos.get(position);
                            if(alerta.getDesc().equals(alertaPos.getDesc())){
                                childDataSnapshot.getRef().removeValue();
                                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AlertasListaActivity.class));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @Override
    public void onClick(View v) {
        if (ocListener != null) {
            ocListener.onClick(v);
        }
    }

    public void asignacionOnClickListener(View.OnClickListener listener) {
        ocListener = listener;
    }

}
