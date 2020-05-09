package com.dam.m21.petsaway.alertas_lista;

import android.os.Parcel;
import android.os.Parcelable;

public class AlertasList implements Parcelable {

    private String tipoAletra,tipoAnimal,color,fecha,raza,desc,fPush,userPush,idFoto;
    private Double latitude,longitude;

    public AlertasList() {
    }

    public String getTipoAletra() {
        return tipoAletra;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public String getColor() {
        return color;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRaza() {
        return raza;
    }

    public String getDesc() {
        return desc;
    }

    public String getfPush() {
        return fPush;
    }

    public String getUserPush() {
        return userPush;
    }

    public String getIdFoto() {
        return idFoto;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public static Creator<AlertasList> getCREATOR() {
        return CREATOR;
    }

    protected AlertasList(Parcel in) {
        tipoAletra = in.readString();
        tipoAnimal = in.readString();
        color = in.readString();
        fecha = in.readString();
        raza = in.readString();
        desc = in.readString();
        fPush = in.readString();
        userPush = in.readString();
        idFoto = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<AlertasList> CREATOR = new Creator<AlertasList>() {
        @Override
        public AlertasList createFromParcel(Parcel in) {
            return new AlertasList(in);
        }

        @Override
        public AlertasList[] newArray(int size) {
            return new AlertasList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipoAletra);
        dest.writeString(tipoAnimal);
        dest.writeString(color);
        dest.writeString(fecha);
        dest.writeString(raza);
        dest.writeString(desc);
        dest.writeString(fPush);
        dest.writeString(userPush);
        dest.writeString(idFoto);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }
}