package com.dam.m21.petsaway.alertas_lista;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListViewHolder>
        implements View.OnClickListener {

private View.OnClickListener ocListener;
private ArrayList<AlertasList> datos;
public ListaAdapter(ArrayList<AlertasList> datos) {
        this.datos = datos;
        }

public static class ListViewHolder extends RecyclerView.ViewHolder{
    private ImageView image_elap;
    private TextView nombre_elap;
    private TextView desc_elap;

    public ListViewHolder(View v) {
        super(v);
        image_elap = v.findViewById(R.id.image_elap);
        nombre_elap = v.findViewById(R.id.nombre_elap);
        desc_elap = v.findViewById(R.id.desc_elap);
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
        nombre_elap.setText(il.getTipoAnimal());
        desc_elap.setText(il.getDesc());
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
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
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
