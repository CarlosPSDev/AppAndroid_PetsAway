package com.dam.m21.petsaway.pojos;

public class PojoFormulario {
    String tipoAletra,tipoAnimal,color,fecha,raza,desc,fPush,userPush,idFoto;
    Double latitude,longitude;
    public PojoFormulario() {
    }
    public PojoFormulario(String tipoAletra, String tipoAnimal, String color, String fecha, String raza, String desc, String fPush, String userPush, String idFoto, Double latitude, Double longitude) {
        this.tipoAletra = tipoAletra;
        this.tipoAnimal = tipoAnimal;
        this.color = color;
        this.fecha = fecha;
        this.raza = raza;
        this.desc = desc;
        this.fPush = fPush;
        this.userPush = userPush;
        this.idFoto = idFoto;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
