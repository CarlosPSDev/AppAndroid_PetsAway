package com.dam.m21.petsaway.perfil_usuario;

public class PojoMascotas {
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
}
