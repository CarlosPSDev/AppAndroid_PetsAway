package com.dam.m21.petsaway.model;

public class PojoUser {
    //////////////////////Comunes////////////////////////
    String nombre,id,status,urlFotoUser;

    //////////////////////Ajustes////////////////////////
    String idFotoFondoChat,tema;

    //////////////////////Mapa////////////////////////
    Double latitude,longitude;

    //////////////////////Chat////////////////////////
    private String search;

    //////////////////////Perfil////////////////////////
    private String email,ciudad,send,receive;
    private boolean visto;

    public PojoUser() {
    }

    public PojoUser(String ciudad,String nombre, String urlFotoUser, String status, String search) {
        this.ciudad = ciudad;
        this.nombre = nombre;
        this.urlFotoUser = urlFotoUser;
        this.status = status;
        this.search = search;
    }
    public PojoUser(String id, String ciudad, String nombre, String urlFotoUser, String status, String search) {
        this.id = id;
        this.ciudad = ciudad;
        this.nombre = nombre;
        this.urlFotoUser = urlFotoUser;
        this.status = status;
        this.search = search;
    }

    public PojoUser(String nombre, boolean visto, String send, String receive, String id, String status) {
        this.nombre = nombre;
        this.visto = visto;
        this.send = send;
        this.receive = receive;
        this.id = id;
        this.status = status;
    }

    public PojoUser(String nombre, String email, String ciudad, String urlFotoUser) {
        this.nombre = nombre;
        this.email = email;
        this.ciudad = ciudad;
        this.urlFotoUser = urlFotoUser;
    }

    public PojoUser(String idFotoFondoChat, String tema,String nombre, Double latitude, Double longitude) {
        this.idFotoFondoChat = idFotoFondoChat;
        this.tema = tema;
        this.nombre = nombre;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getUrlFotoUser() {
        return urlFotoUser;
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

    public String getSearch() {
        return search;
    }

    public String getEmail() {
        return email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getSend() {
        return send;
    }

    public String getReceive() {
        return receive;
    }

    public boolean isVisto() {
        return visto;
    }
}
