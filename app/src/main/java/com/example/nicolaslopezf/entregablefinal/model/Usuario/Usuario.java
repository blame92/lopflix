package com.example.nicolaslopezf.entregablefinal.model.Usuario;

import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class Usuario {

    private String id;
    private String foto;
    private String nombre;
    private String email;
//    private ArrayList<String> amigos;
//    private ArrayList<String> watchlist;

    public Usuario() {
    }

    public Usuario(String id, String foto, String nombre, String email) {
        this.id = id;
        this.foto = foto;
        this.nombre = nombre;
        this.email = email;
    }

    public Usuario(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public ArrayList<String> getAmigos() {
//        return amigos;
//    }
//
//    public void setAmigos(ArrayList<String> amigos) {
//        this.amigos = amigos;
//    }
//
//    public ArrayList<Pelicula> getWatchlist() {
//        return watchlist;
//    }
//
//    public void setWatchlist(ArrayList<Pelicula> watchlist) {
//        this.watchlist = watchlist;
//    }
}
