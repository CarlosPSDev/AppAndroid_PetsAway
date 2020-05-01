package com.dam.m21.petsaway.perfil_usuario;

public class PojoUsuario {
    private String nombre;
    private String email;
    private String ciudad;
    private String url;
    private boolean visto;
    private String send;
    private String receive;
    private int id;
    private String status;

    public PojoUsuario(String nombre, boolean visto, String send, String receive, int id, String status) {
        this.nombre = nombre;
        this.visto = visto;
        this.send = send;
        this.receive = receive;
        this.id = id;
        this.status = status;
    }

    public PojoUsuario(String nombre, String email, String ciudad, String url) {
        this.nombre = nombre;
        this.email = email;
        this.ciudad = ciudad;
        this.url = url;
    }
    public PojoUsuario() {}

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getUrl() {
        return url;
    }
}
