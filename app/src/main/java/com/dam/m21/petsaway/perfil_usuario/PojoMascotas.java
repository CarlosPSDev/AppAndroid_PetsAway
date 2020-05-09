package com.dam.m21.petsaway.perfil_usuario;

import android.os.Parcel;
import android.os.Parcelable;

public class PojoMascotas implements Parcelable {
    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private String identificacion;
    private String sexo;
    private String descrip;
    private String fechaNac;
    private String urlImg;

    public PojoMascotas(String nombre, String especie, String raza, String color, String identificacion, String sexo, String descrip, String fechaNac) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.color = color;
        this.identificacion = identificacion;
        this.sexo = sexo;
        this.descrip = descrip;
        this.fechaNac = fechaNac;
    }
    public PojoMascotas(String nombre, String urlImg) {
        this.nombre = nombre;
        this.urlImg = urlImg;
    }
    public PojoMascotas() {}

    protected PojoMascotas(Parcel in) {
        nombre = in.readString();
        especie = in.readString();
        raza = in.readString();
        color = in.readString();
        identificacion = in.readString();
        sexo = in.readString();
        descrip = in.readString();
        fechaNac = in.readString();
        urlImg = in.readString();
    }

    public static final Creator<PojoMascotas> CREATOR = new Creator<PojoMascotas>() {
        @Override
        public PojoMascotas createFromParcel(Parcel in) {
            return new PojoMascotas(in);
        }

        @Override
        public PojoMascotas[] newArray(int size) {
            return new PojoMascotas[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public String getColor() {
        return color;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getSexo() {
        return sexo;
    }
    public String getFechaNac() {
        return fechaNac;
    }

    public String getUrlImg() {return urlImg;}

    public String getEspecie() {
        return especie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(especie);
        dest.writeString(raza);
        dest.writeString(color);
        dest.writeString(identificacion);
        dest.writeString(sexo);
        dest.writeString(descrip);
        dest.writeString(fechaNac);
        dest.writeString(urlImg);
    }
}
