package com.dam.m21.petsaway.model;

public class PojoUser {
    String idFotoFondoChat,tema,userNombre;
    Double latitude,longitude;

    public PojoUser() {
    }

    public PojoUser(String idFotoFondoChat, String tema,String userNombre, Double latitude, Double longitude) {
        this.idFotoFondoChat = idFotoFondoChat;
        this.tema = tema;
        this.userNombre = userNombre;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserNombre() {
        return userNombre;
    }

    public String getIdFotoFondoChat() {
        return idFotoFondoChat;
    }

    public String getTema() {
        return tema;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
