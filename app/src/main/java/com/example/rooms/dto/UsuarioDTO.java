package com.example.rooms.dto;

import com.google.firebase.database.Exclude;

public class UsuarioDTO {

    @Exclude
    private String uid;

    private String nombre;
    private String apellido;
    private String correo;
    private String TI;
    private String rol;
    private Integer creditos;
    private Long timestampSiguienteRecarga;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String apellido, String correo, String TI, String rol, Integer creditos, Long timestampSiguienteRecarga) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.TI = TI;
        this.rol = rol;
        this.creditos = creditos;
        this.timestampSiguienteRecarga = timestampSiguienteRecarga;
    }

    public String getUid() {return uid;}
    public void setUid(String uid) {this.uid = uid;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}
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
}
