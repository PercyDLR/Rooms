package com.example.rooms.dto;

import java.io.Serializable;

public class ReservaDTO implements Serializable {

    private String keyEspacio;
    private String dia;
    private Integer horaInicio;
    private Integer horaFin;
    private String nombreEspacio;

    public String getKeyEspacio() {return keyEspacio;}
    public void setKeyEspacio(String keyEspacio) {this.keyEspacio = keyEspacio;}
    public String getDia() {return dia;}
    public void setDia(String dia) {this.dia = dia;}
    public Integer getHoraInicio() {return horaInicio;}
    public void setHoraInicio(Integer horaInicio) {this.horaInicio = horaInicio;}
    public Integer getHoraFin() {return horaFin;}
    public void setHoraFin(Integer horaFin) {this.horaFin = horaFin;}
    public String getNombreEspacio() {return nombreEspacio;}
    public void setNombreEspacio(String nombreEspacio) {this.nombreEspacio = nombreEspacio;}
}
