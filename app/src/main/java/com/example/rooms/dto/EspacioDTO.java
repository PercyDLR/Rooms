package com.example.rooms.dto;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EspacioDTO implements Serializable {

    @Exclude
    private String key;

    private String nombre;
    private String descripcion;
    private Integer creditosPorHora;
    private String imgUrl;
    private Integer imgSize;
    private Integer horariosDisponibles;
    private Boolean activo;


    public EspacioDTO() {}

    public EspacioDTO(String nombre, String descripcion, Integer creditosPorHora, Integer horariosDisponibles, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditosPorHora = creditosPorHora;
        this.horariosDisponibles = horariosDisponibles;
        this.activo = activo;
    }

    public EspacioDTO(String nombre, String descripcion, Integer creditosPorHora, String imgUrl, Integer imgSize, Integer horariosDisponibles, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditosPorHora = creditosPorHora;
        this.imgUrl = imgUrl;
        this.imgSize = imgSize;
        this.activo = activo;
        this.horariosDisponibles = horariosDisponibles;
    }

    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("nombre",this.nombre);
        map.put("key",this.key);
        map.put("descripcion",this.descripcion);
        map.put("creditosPorHora",this.creditosPorHora);
        map.put("imgUrl",this.imgUrl);
        map.put("imgSize",this.imgSize);
        map.put("activo",this.activo);
        map.put("horariosDisponibles",this.horariosDisponibles);

        return map;
    }

    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public Integer getCreditosPorHora() {return creditosPorHora;}
    public void setCreditosPorHora(Integer creditosPorHora) {this.creditosPorHora = creditosPorHora;}
    public String getImgUrl() {return imgUrl;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}
    public Boolean isActivo() {return activo;}
    public void setActivo(Boolean activo) {this.activo = activo;}
    public Integer getHorariosDisponibles() {return horariosDisponibles;}
    public void setHorariosDisponibles(Integer horariosDisponibles) {this.horariosDisponibles = horariosDisponibles;}
    public Integer getImgSize() {return imgSize;}
    public void setImgSize(Integer imgSize) {this.imgSize = imgSize;}
}
