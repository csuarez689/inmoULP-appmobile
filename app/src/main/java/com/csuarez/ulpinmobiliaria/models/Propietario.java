package com.csuarez.ulpinmobiliaria.models;

import java.io.Serializable;

public class Propietario  implements Serializable {

    private int idPropietario;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    public Propietario(int idPropietario, String nombre, String apellido, String dni, String telefono, String email) {
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    public Propietario(){}

    public int getIdPropietario() { return idPropietario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getNombreCompleto() { return nombre + " " + apellido; }
}
