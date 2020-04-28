package com.dam.m21.petsaway.perfil_usuario;

public class PojoPruebasCarlos {
    private String nombre;
    private String raza;
    private String color;
    private String identificacion;
    private String descrip;
    private int edad;
    private String urlImg;

    public PojoPruebasCarlos(String nombre, String raza, String color, String identificacion, String descrip, int edad, String urlImg) {
        this.nombre = nombre;
        this.raza = raza;
        this.color = color;
        this.identificacion = identificacion;
        this.descrip = descrip;
        this.edad = edad;
        this.urlImg = urlImg;
    }
    public PojoPruebasCarlos(String nombre, String urlImg) {
        this.nombre = nombre;
        this.urlImg = urlImg;
    }
    public PojoPruebasCarlos() {}

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

    public int getEdad() {
        return edad;
    }

    public String getUrlImg() {return urlImg;}
}
