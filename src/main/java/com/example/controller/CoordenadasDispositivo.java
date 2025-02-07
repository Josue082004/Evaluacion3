package com.example.controller;

import java.util.HashMap;
import com.example.models.Dispositivo;


import java.util.HashMap;

import java.util.HashMap;

public class CoordenadasDispositivo {
    private HashMap<String, Dispositivo> Dispositivos;

    public CoordenadasDispositivo() {
        Dispositivos = new HashMap<>();
    }

    public void agregarDispositivo(Dispositivo Dispositivo) {
        Dispositivos.put(Dispositivo.getNombre(), Dispositivo);
    }

    public Dispositivo obtenerDispositivo(String nombre) {
        return Dispositivos.get(nombre);
    }

    public double calcularCosteTransmision(String nombre1, String nombre2) {
        Dispositivo equipo1 = Dispositivos.get(nombre1);
        Dispositivo equipo2 = Dispositivos.get(nombre2);

        if (equipo1 != null && equipo2 != null) {
            double mediaTransmision1 = (equipo1.getVelocidadAlta() + equipo1.getVelocidadBaja()) / 2.0;
            double mediaTransmision2 = (equipo2.getVelocidadAlta() + equipo2.getVelocidadBaja()) / 2.0;

            double mediaCapacidadTransmision = (mediaTransmision1 + mediaTransmision2) / 2.0;

            if (mediaCapacidadTransmision > 0) {
                return 3600 / mediaCapacidadTransmision;
            }
        }
        return -1; 
    }
}


