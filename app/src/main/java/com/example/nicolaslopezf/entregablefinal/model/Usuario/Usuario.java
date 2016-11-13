package com.example.nicolaslopezf.entregablefinal.model.Usuario;

import com.example.nicolaslopezf.entregablefinal.model.Pelicula;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class Usuario {

    private String userName;
    private String password;
    private String age;
    private Integer userImage;
    private String favGenre;
    private ArrayList<Usuario> friends;
    private ArrayList<Pelicula> misPeliculas;


    public Usuario(String userName, String password, String age, Integer userImage, String favGenre, ArrayList<Usuario> friends, ArrayList<Pelicula> misPeliculas) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.userImage = userImage;
        this.favGenre = favGenre;
        this.friends = friends;
        this.misPeliculas = misPeliculas;
    }

    public Usuario(String userName, String age, String favGenre) {
        this.userName = userName;
        this.age = age;
        this.favGenre = favGenre;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getUserImage() {
        return userImage;
    }

    public void setUserImage(Integer userImage) {
        this.userImage = userImage;
    }

    public String getFavGenre() {
        return favGenre;
    }

    public void setFavGenre(String favGenre) {
        this.favGenre = favGenre;
    }

    public ArrayList<Usuario> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Usuario> friends) {
        this.friends = friends;
    }

    public ArrayList<Pelicula> getMisPeliculas() {
        return misPeliculas;
    }

    public void setMisPeliculas(ArrayList<Pelicula> misPeliculas) {
        this.misPeliculas = misPeliculas;
    }
}
