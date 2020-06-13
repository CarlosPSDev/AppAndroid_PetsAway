package com.dam.m21.petsaway.alertas_adoptar;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_lista.AlertasListaActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaAdoptaAdapter extends RecyclerView.Adapter<ListaAdoptaAdapter.ListViewHolder>
        implements View.OnClickListener {

private View.OnClickListener ocListener;
private ArrayList<AlertasList> datos;
    private AlertDialog.Builder ad;


public ListaAdoptaAdapter(ArrayList<AlertasList> datos) {
        this.datos = datos;
        }

public static class ListViewHolder extends RecyclerView.ViewHolder{
    private ImageView image_adopt;
    private TextView tipoAn_adopt;
    private TextView edad_adopt;
    ImageButton bt_borrarAlerta;
    LinearLayout ll_adopt;
    ImageView sexo_adopt;

    public ListViewHolder(View v) {
        super(v);
        image_adopt = v.findViewById(R.id.image_adopt);
        tipoAn_adopt = v.findViewById(R.id.tipoAn_adopt);
        edad_adopt = v.findViewById(R.id.edad_adopt);
        ll_adopt=v.findViewById(R.id.ll_adopt);
        sexo_adopt=v.findViewById(R.id.sexo_adopt);
        bt_borrarAlerta=v.findViewById(R.id.bt_borrarAdopcion);
    }

    public void bindAList(AlertasList il){
        Glide.with(itemView.getContext())
            .load(il.getFotos().get(0).getUrl())
                    .into(image_adopt);
        if (il.getSexo().equals("m")){
            sexo_adopt.setImageResource(R.drawable.ic_macho);
        }else{
            sexo_adopt.setImageResource(R.drawable.ic_hembra);
        }
        FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
        assert fu!=null;
        if(il.getUserPush().equals(fu.getEmail())){
            bt_borrarAlerta.setVisibility(View.VISIBLE);
        }else {
            bt_borrarAlerta.setVisibility(View.GONE);
        }
        edad_adopt.setText(il.getEdad());
        tipoAn_adopt.setText(il.getTipoAnimal());
    }
}

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_adopta, parent, false);
        v.setOnClickListener(this);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        holder.bindAList(datos.get(position));
        holder.bt_borrarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ad=new AlertDialog.Builder(holder.itemView.getContext());
                ad.setCancelable(false);
                ad.setMessage(R.string.msjDialogBorrarAlerta);
                ad.setPositiveButton(R.string.msjBtDialogSI, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase.getInstance().getReference().child("adoptar").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    AlertasList alerta=childDataSnapshot.getValue(AlertasList.class);
                                    AlertasList alertaPos=datos.get(position);
                                    assert alerta!=null;
                                    if(alerta.getDesc().equals(alertaPos.getDesc())){
                                        childDataSnapshot.getRef().removeValue();
                                        ((AdoptaListaActivity)holder.itemView.getContext()).finish();
                                        holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AdoptaListaActivity.class));
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
                ad.setNegativeButton(R.string.msjBtDialogNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                ad.create().show();

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
