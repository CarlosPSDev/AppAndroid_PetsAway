package com.dam.m21.petsaway.perfil_usuario;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;
import java.util.ArrayList;

public class AdapterPetsProfile extends RecyclerView.Adapter<AdapterPetsProfile.ViewHolderPets> implements View.OnClickListener{
    ArrayList<PojoMascotas>listaMascotas;
    Context context;
    View.OnClickListener aaListener;

    public AdapterPetsProfile(ArrayList<PojoMascotas> listaMascotas, Context contexto) {
        this.listaMascotas = listaMascotas;
        context = contexto;
    }

    @NonNull
    @Override
    public ViewHolderPets onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_profile, null, false);
        view.setOnClickListener(aaListener);
        return new ViewHolderPets(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPets holder, int position) {
        holder.asignarDatos(listaMascotas.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    @Override
    public void onClick(View v) {
        if (aaListener != null) aaListener.onClick(v);
    }

    public void asignacionOnclickListener(View.OnClickListener listener){
        aaListener = listener;
    }

    public class ViewHolderPets extends RecyclerView.ViewHolder {
        ImageView fotoMascota;
        TextView nombreMascota;
        TextView especieMascota;
        TextView colorMascota;
        TextView fechaNacMascota;
        ImageView ivSexoMascota;

        public ViewHolderPets(@NonNull View itemView) {
            super(itemView);
            fotoMascota = itemView.findViewById(R.id.ivRcPet);
            nombreMascota = itemView.findViewById(R.id.tvRcNombre);
            especieMascota = itemView.findViewById(R.id.tvRcValEspecie);
            colorMascota = itemView.findViewById(R.id.tvRcValColor);
            fechaNacMascota = itemView.findViewById(R.id.tvRcValFechaNac);
            ivSexoMascota = itemView.findViewById(R.id.ivSexoMascota);
        }

        public void asignarDatos(PojoMascotas mascota) {
            if (mascota.getUrlImg() != null) {
                Glide.with(context).load(mascota.getUrlImg())
                        .into(fotoMascota);
            }
            nombreMascota.setText(mascota.getNombre());
            especieMascota.setText(mascota.getEspecie());
            colorMascota.setText(mascota.getColor());
            fechaNacMascota.setText("Nac. " + mascota.getFechaNac());

            if (mascota.getSexo().trim().equalsIgnoreCase("macho")) {
                ivSexoMascota.setImageResource(R.drawable.ic_macho);

            } else if (mascota.getSexo().trim().equalsIgnoreCase("hembra")) {
                ivSexoMascota.setImageResource(R.drawable.ic_hembra);
            }
        }
    }
}
