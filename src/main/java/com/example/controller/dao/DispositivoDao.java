package com.example.controller.dao;

import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Graph;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

import java.io.File;
import java.io.FileWriter;

import com.example.controller.dao.implement.AdapterDao;
import com.example.models.Dispositivo;

public class DispositivoDao extends AdapterDao<Dispositivo> {
    private Dispositivo Dispositivo;
    private LinkedList<Dispositivo> listAll;

    public DispositivoDao() {
        super(Dispositivo.class);
    }
    public Dispositivo getDispositivo() {
        if (Dispositivo == null) {
            Dispositivo = new Dispositivo();
        }
        return this.Dispositivo;
    }

    public void setDispositivo(Dispositivo Dispositivo) {
        this.Dispositivo = Dispositivo;
    }
    
    public LinkedList getListAll() {
        if(listAll == null){
            this.listAll = (LinkedList<Dispositivo>) listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize()+1;
        Dispositivo.setId(id);
        this.persist(this.Dispositivo);
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getDispositivo(), getDispositivo().getId());
        return true;
    }

    public LinkedList<Dispositivo> buscar_nombre(String texto) {
        LinkedList<Dispositivo> lista = new LinkedList<>();
        LinkedList<Dispositivo> listita = listAll();
        if (!listita.isEmpty()) {
            Dispositivo[] aux = listita.toArray();
            for (int i = 0; i < aux.length; i++) {

                if (aux[i].getNombre().toLowerCase().startsWith(texto.toLowerCase())) {
                    lista.add(aux[i]);
                }
            }
        }
        return lista;
    }

    

    



        
}
