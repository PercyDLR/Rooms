package com.example.rooms.dto;

import com.google.firebase.database.Exclude;

public class EspacioDTO {

    @Exclude
    String key;

    String nombre;
    String descripción;
    Integer creditosPorHora;
    String imgUrl;
    Integer horariosDisponibles;
    Boolean activo;

    public EspacioDTO() {}

    public EspacioDTO(String key, String nombre, String descripción, Integer creditosPorHora, String imgUrl, Integer horariosDisponibles, Boolean activo) {
        this.key = key;
        this.nombre = nombre;
        this.descripción = descripción;
        this.creditosPorHora = creditosPorHora;
        this.imgUrl = imgUrl;
        this.activo = activo;
        this.horariosDisponibles = horariosDisponibles;
    }

    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripción() {return descripción;}
    public void setDescripción(String descripción) {this.descripción = descripción;}
    public Integer getCreditosPorHora() {return creditosPorHora;}
    public void setCreditosPorHora(Integer creditosPorHora) {this.creditosPorHora = creditosPorHora;}
    public String getImgUrl() {return imgUrl;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}
    public Boolean isActivo() {return activo;}
    public void setActivo(Boolean activo) {this.activo = activo;}
}
