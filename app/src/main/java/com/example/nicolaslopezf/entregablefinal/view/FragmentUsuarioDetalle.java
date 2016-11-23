package com.example.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/23/2016.
 */

public class FragmentUsuarioDetalle extends Fragment {

    private RecyclerView recyclerViewUsuarios;
    private AdapterRecyclePeliculasFavoritos unAdapterFavoritasUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundleRecibido = getArguments();
        String idUsuarioSeleccionado = bundleRecibido.getString("idClick");
        final View viewADevolverInflado = inflater.inflate(R.layout.fragment_usuario_detalle, container, false);
        recyclerViewUsuarios = (RecyclerView) viewADevolverInflado.findViewById(R.id.fragment_usuario_favoritosAmigos);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerViewUsuarios.setLayoutManager(linearLayoutManager);
        unAdapterFavoritasUsuario = new AdapterRecyclePeliculasFavoritos(getActivity());
        unAdapterFavoritasUsuario.setListener(new ListenerPeliculasSoloImagen(recyclerViewUsuarios,unAdapterFavoritasUsuario));
        final ArrayList<Pelicula> peliculas = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("users").child(idUsuarioSeleccionado).child("watchlist").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userDataSnapshot : dataSnapshot.getChildren()){
                            Pelicula pelicula = userDataSnapshot.getValue(Pelicula.class);
                            peliculas.add(pelicula);
                            unAdapterFavoritasUsuario.notifyDataSetChanged();
                        }



                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

        unAdapterFavoritasUsuario.setListaDePeliculas(peliculas);
        recyclerViewUsuarios.setAdapter(unAdapterFavoritasUsuario);


        return viewADevolverInflado;


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

            FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity unComunicador = (FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity) getActivity();
            unComunicador.clickearonEstaPeliculaDeFavoritos(peliculaTocada);


        }
    }





}
