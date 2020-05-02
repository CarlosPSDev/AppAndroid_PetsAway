package com.dam.m21.petsaway.perfil_usuario;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.m21.petsaway.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AniadirMascota extends Fragment {
    ImageView imagenMascota;
    EditText etNombreM;
    Spinner spinEspecie;
    EditText etRazaM;
    EditText etColor;
    EditText etIdM;
    EditText etDescripcion;
    EditText etFecha;
    EditText etTipoM;
    String especieSeleccionada;
    String urlM;
    Button btnGuardar;
    Button btnCancelar;
    Button cargarImagen;
    private PerfilListener mListener;

    public AniadirMascota() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_aniadir_mascota, container, false);
        especieSeleccionada = "";
        btnGuardar = v.findViewById(R.id.btnGuardarMascA);
        etNombreM = v.findViewById(R.id.etNomMascA);
        etRazaM = v.findViewById(R.id.etRazaMascA);
        etColor = v.findViewById(R.id.etColorMascA);
        etIdM = v.findViewById(R.id.etIdMascA);
        etDescripcion = v.findViewById(R.id.etDescMascA);
        etFecha = v.findViewById(R.id.etDescMascA);
        btnCancelar = v.findViewById(R.id.btnCancelarMascA);
        spinEspecie = v.findViewById(R.id.spinnerEspecies);
        etTipoM = v.findViewById(R.id.etTipoMascA);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean especieOk = true;
                String nombreM = etNombreM.getText().toString();
                String razaM = etRazaM.getText().toString();
                String colorM = etColor.getText().toString();
                String idM = etIdM.getText().toString();
                String descripM = etDescripcion.getText().toString();
                String fechaM = etFecha.getText().toString();
                String especieOtro = "";
                if (especieSeleccionada.equals("otro") & especieOtro.isEmpty()) especieOk = false;

                if (!especieSeleccionada.equals("-*especie-") & !nombreM.isEmpty() & !razaM.isEmpty()
                & !!fechaM.isEmpty() & especieOk){
                    PojoMascotas mascotaGuardar = new PojoMascotas(nombreM, especieSeleccionada, razaM, colorM, idM, descripM, fechaM, urlM);
                    mListener.guardarMascota(mascotaGuardar);
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_datos_vacios_m), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelarOpcion();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.especies_animales,
                android.R.layout.simple_spinner_item);
        spinEspecie.setAdapter(adapter);

        spinEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override //Ojo, aunque no pinches ya selecciona el primer item
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = (String) parent.getItemAtPosition(position);
                Toast.makeText(parent.getContext(), "Se ha seleccionado " + seleccion, Toast.LENGTH_SHORT).show();
                especieSeleccionada = seleccion;
                Log.d("Seleccion", especieSeleccionada );
                if (seleccion.equals("Otro")) habilitarEditext(true);
                else habilitarEditext(false);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               return;
            }
        });
        return v;
    }

    private void habilitarEditext(boolean habilitar){
        etTipoM.setEnabled(habilitar);
        if (habilitar) etTipoM.setVisibility(View.VISIBLE);
        else {
            etTipoM.setVisibility(View.INVISIBLE);
            etTipoM.setText("");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PerfilListener) {
            mListener = (PerfilListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
