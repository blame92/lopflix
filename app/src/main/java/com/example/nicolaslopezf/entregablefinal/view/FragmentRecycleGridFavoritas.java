package com.example.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/7/2016.
 */

public class FragmentRecycleGridFavoritas extends Fragment {

    private RecyclerView recyclerViewPeliculas;
    private AdapterRecyclePeliculasFavoritos unAdapterPelicula;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);

        recyclerViewPeliculas = (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewPeliculas.setLayoutManager(linearLayoutManager);

        PeliculaController peliculasController = new PeliculaController();

        //voy a  hacer que le pase un bundle cuando clikias el boton y eel metodo reciba el bundle y cree el metodo basado en eso
        ArrayList<Pelicula> peliculasDelRecycle = peliculasController.obtenerFavoritasDeBD(getActivity());



        unAdapterPelicula = new AdapterRecyclePeliculasFavoritos(getActivity());
        unAdapterPelicula.setListaDePeliculas(peliculasDelRecycle);
        unAdapterPelicula.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculas,unAdapterPelicula));
        recyclerViewPeliculas.setAdapter(unAdapterPelicula);



        return unaVistaADevolver;
    }
    public class ListenerPeliculasSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclePeliculasFavoritos adapterAUsar;

        public ListenerPeliculasSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclePeliculasFavoritos adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            Pelicula peliculaTocada = adapterAUsar.getPeliculaAtPosition(posicionTocada);

            Log.d("imagen",peliculaTocada.getPoster());

            ComunicadorFavoritosActivity unComunicador = (ComunicadorFavoritosActivity) getActivity();
            unComunicador.clickearonEstaPeliculaDeFavoritos(peliculaTocada);


        }
    }
    public interface ComunicadorFavoritosActivity{
        void clickearonEstaPeliculaDeFavoritos(Pelicula peliculaClickeada);

    }


}
