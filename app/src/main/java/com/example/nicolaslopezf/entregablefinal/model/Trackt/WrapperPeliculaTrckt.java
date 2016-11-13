package com.example.nicolaslopezf.entregablefinal.model.Trackt;

import com.example.nicolaslopezf.entregablefinal.model.Pelicula;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagoiraola on 10/26/16.
 */

public class WrapperPeliculaTrckt {

    @SerializedName("results")
    private ArrayList<PeliculaTrckt> peliculaArrayList;

    public ArrayList<PeliculaTrckt> getPeliculaArrayList() {
        return peliculaArrayList;
    }
}
