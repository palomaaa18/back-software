package com.madurez.back_software.dtos;

public class CrearUsuarioRequest {

    private String nombre;
    private String email;
    private String password;
    private String rol; // se recibe como texto y se valida/convierte en el service

    public CrearUsuarioRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}