package com.example.rooms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class HorarioDTO implements Serializable {

    private Long fecha;
    private HashMap<String,Boolean> listaHoras;

    public HorarioDTO() {}

    public HorarioDTO(Long fecha, HashMap<String,Boolean> listaHoras) {
        this.fecha = fecha;
        this.listaHoras = listaHoras;
    }

    public Long getFecha() {return fecha;}
    public void setFecha(Long fecha) {this.fecha = fecha;}
    public HashMap<String,Boolean> getListaHoras() {return listaHoras;}
    public void setListaHoras(HashMap<String,Boolean> listaHoras) {this.listaHoras = listaHoras;}

}
