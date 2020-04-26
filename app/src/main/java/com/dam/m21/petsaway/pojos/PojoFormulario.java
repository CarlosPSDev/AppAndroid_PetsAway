package com.dam.m21.petsaway.pojos;

public class PojoFormulario {
    String tipoAletra,tipoAnimal,color,fecha,raza,desc,fPush,userPush;
    Double latitude,longitude;
    public PojoFormulario() {
    }
    public PojoFormulario(String tipoAletra, String tipoAnimal, String color, String fecha, String raza, String desc, String fPush, String userPush, Double latitude, Double longitude) {
        this.tipoAletra = tipoAletra;
        this.tipoAnimal = tipoAnimal;
        this.color = color;
        this.fecha = fecha;
        this.raza = raza;
        this.desc = desc;
        this.fPush = fPush;
        this.userPush = userPush;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTipoAletra() {
        return tipoAletra;
    }

    public void setTipoAletra(String tipoAletra) {
        this.tipoAletra = tipoAletra;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }
}
