package com.dam.m21.petsaway.onBoarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.m21.petsaway.R;

import java.util.ArrayList;

public class AdapterPetsProfile extends RecyclerView.Adapter<AdapterPetsProfile.ViewHolderPets> {
    ArrayList<PojoPruebasCarlos>listaMascotas;

    public AdapterPetsProfile(ArrayList<PojoPruebasCarlos> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public ViewHolderPets onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_profile, null, false);
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

    public class ViewHolderPets extends RecyclerView.ViewHolder {
        ImageView fotoMascota;
        TextView nombreMascota;
        public ViewHolderPets(@NonNull View itemView) {
            super(itemView);
            fotoMascota = itemView.findViewById(R.id.ivRcPet);
            nombreMascota = itemView.findViewById(R.id.tvRcNombre);
        }

        public void asignarDatos(PojoPruebasCarlos pojoPruebasCarlos) {
            fotoMascota.setImageResource(Integer.parseInt(pojoPruebasCarlos.getUrlImg()));
            nombreMascota.setText(pojoPruebasCarlos.getNombre());
        }
    }
}
