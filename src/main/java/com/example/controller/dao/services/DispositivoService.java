package com.example.controller.dao.services;

import java.util.HashMap;


import com.example.controller.dao.DispositivoDao;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Dispositivo;

public class DispositivoService {

    private DispositivoDao obj;
    private LinkedList<Dispositivo> Dispositivos;
    public DispositivoService() {
        obj = new DispositivoDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList listAll() {
        return obj.getListAll();
    }

    public Dispositivo getDispositivo() {
        return obj.getDispositivo();
    }

    public void setDispositivo(Dispositivo Dispositivo) {
        obj.setDispositivo(Dispositivo);
    }

    public Dispositivo get(Integer id) throws Exception {
        return obj.get(id);
    }

    public Dispositivo findByName(String nombre) {
        for (Dispositivo v : Dispositivos) {
            if (v.getNombre().equalsIgnoreCase(nombre)) {
                return v; 
            }
        }
        return null; 
    }

    

}
