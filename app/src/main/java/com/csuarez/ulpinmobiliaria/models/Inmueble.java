package com.csuarez.ulpinmobiliaria.models;

import java.io.Serializable;

public class Inmueble implements Serializable {
    
    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private int superficie;
    private double latitud;
    private double longitud;
    private double valor;
    private String imagen;
    private Boolean disponible;
    private int idPropietario;
    private Propietario duenio;
    private boolean tieneContratoVigente;

    public Inmueble() {}

    public Inmueble(int idInmueble, String direccion, String uso, String tipo, int ambientes, 
                    int superficie, double latitud, double longitud, double valor, String imagen, 
                    Boolean disponible, int idPropietario, Propietario duenio, boolean tieneContratoVigente) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.uso = uso;
        this.tipo = tipo;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.valor = valor;
        this.imagen = imagen;
        this.disponible = disponible;
        this.idPropietario = idPropietario;
        this.duenio = duenio;
        this.tieneContratoVigente = tieneContratoVigente;
    }

    public int getIdInmueble() { return idInmueble; }
    public String getDireccion() { return direccion; }
    public String getUso() { return uso; }
    public String getTipo() { return tipo; }
    public int getAmbientes() { return ambientes; }
    public int getSuperficie() { return superficie; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public double getValor() { return valor; }
    public String getImagen() { return imagen; }
    public Boolean getDisponible() { return disponible; }
    public int getIdPropietario() { return idPropietario; }
    public Propietario getDuenio() { return duenio; }
    public boolean isTieneContratoVigente() { return tieneContratoVigente; }

    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setUso(String uso) { this.uso = uso; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setValor(double valor) { this.valor = valor; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }
    public void setDuenio(Propietario duenio) { this.duenio = duenio; }
    public void setTieneContratoVigente(boolean tieneContratoVigente) { this.tieneContratoVigente = tieneContratoVigente; }
}