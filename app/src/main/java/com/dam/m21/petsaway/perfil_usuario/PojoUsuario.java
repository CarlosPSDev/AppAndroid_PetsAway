package com.dam.m21.petsaway.perfil_usuario;

public class PojoUsuario {
    private String nombre;
    private String email;
    private String ciudad;
    private String url;

    public PojoUsuario(String nombre, String email, String ciudad, String url) {
        this.nombre = nombre;
        this.email = email;
        this.ciudad = ciudad;
        this.url = url;
    }

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
