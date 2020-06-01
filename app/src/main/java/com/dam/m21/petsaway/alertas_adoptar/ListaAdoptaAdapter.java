package com.dam.m21.petsaway.alertas_adoptar;

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
import com.dam.m21.petsaway.alertas_lista.AlertasList;
import com.dam.m21.petsaway.alertas_map.AlertasMapaActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListaAdoptaAdapter extends RecyclerView.Adapter<ListaAdoptaAdapter.ListViewHolder>
        implements View.OnClickListener {

private View.OnClickListener ocListener;
private ArrayList<AlertasList> datos;

    public ListaAdoptaAdapter(ArrayList<AlertasList> datos) {
        this.datos = datos;
        }

public static class ListViewHolder extends RecyclerView.ViewHolder{
    private ImageView image_adopt;
    private TextView tipoAn_adopt;
    private TextView edad_adopt;
    LinearLayout ll_adopt;
    ImageView sexo_adopt;

    public ListViewHolder(View v) {
        super(v);
        image_adopt = v.findViewById(R.id.image_adopt);
        tipoAn_adopt = v.findViewById(R.id.tipoAn_adopt);
        edad_adopt = v.findViewById(R.id.edad_adopt);
        ll_adopt=v.findViewById(R.id.ll_adopt);
        sexo_adopt=v.findViewById(R.id.sexo_adopt);
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
        ListViewHolder avh = new ListViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        holder.bindAList(datos.get(position));
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
