package com.dam.m21.petsaway.alertas_adoptar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.m21.petsaway.R;

import java.util.ArrayList;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.ListViewHolder> implements View.OnClickListener {

	private View.OnClickListener ocListener;
	private ArrayList<Fotos> datos;

	public FotosAdapter(ArrayList<Fotos> datos) {
		this.datos = datos;
	}

	public static class ListViewHolder extends RecyclerView.ViewHolder {
		private ImageView f_adopt;

		public ListViewHolder(View v) {
			super(v);
			f_adopt = v.findViewById(R.id.f_adopt);
		}

		public void bindAList(Fotos il) {
					Glide.with(itemView.getContext())
							.load(il.getUrl())
							.into(f_adopt);
		}

	}
		@NonNull
		@Override
		public FotosAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View v;
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_fotos_adopta, parent, false);
			v.setOnClickListener(this);
			FotosAdapter.ListViewHolder avh = new FotosAdapter.ListViewHolder(v);
			return avh;
		}

		@Override
		public void onBindViewHolder(@NonNull final FotosAdapter.ListViewHolder holder, final int position) {
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
