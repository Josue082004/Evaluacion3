package com.example.models;

public class Dispositivo {
    private Integer id;
    private Double velocidadAlta;
    private Double velocidadBaja;
    private String ip;
    private String nombre;
    private String marca;

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Double getVelocidadAlta() {
    return this.velocidadAlta;
  }

  public void setVelocidadAlta(Double velocidadAlta) {
    this.velocidadAlta = velocidadAlta;
  }

  public Double getVelocidadBaja() {
    return this.velocidadBaja;
  }

  public void setVelocidadBaja(Double velocidadBaja) {
    this.velocidadBaja = velocidadBaja;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getMarca() {
    return this.marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }


    public Dispositivo() {
    }

    
}
