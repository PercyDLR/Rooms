package com.example.rooms.dto;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

    @Exclude
    private String uid;

    private String nombre;
    private String correo;
    private String TI;
    private String rol;
    private Integer creditos;
    private Long timestampSiguienteRecarga;
    private Boolean activo;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String correo, String TI, String rol, Integer creditos, Long timestampSiguienteRecarga, Boolean activo) {
        this.nombre = nombre;
        this.correo = correo;
        this.TI = TI;
        this.rol = rol;
        this.creditos = creditos;
        this.timestampSiguienteRecarga = timestampSiguienteRecarga;
        this.activo = activo;
    }

    public String getUid() {return uid;}
    public void setUid(String uid) {this.uid = uid;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}
    public String getTI() {return TI;}
    public void setTI(String TI) {this.TI = TI;}
    public String getRol() {return rol;}
    public void setRol(String rol) {this.rol = rol;}
    public Integer getCreditos() {return creditos;}
    public void setCreditos(Integer creditos) {this.creditos = creditos;}
    public Long getTimestampSiguienteRecarga() {return timestampSiguienteRecarga;}
    public void setTimestampSiguienteRecarga(Long timestampSiguienteRecarga) {this.timestampSiguienteRecarga = timestampSiguienteRecarga;}
    public Boolean isActivo() {return activo;}
    public void setActivo(Boolean activo) {this.activo = activo;}
}
