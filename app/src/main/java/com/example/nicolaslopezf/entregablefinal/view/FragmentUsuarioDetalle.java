package com.example.nicolaslopezf.entregablefinal.view;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/23/2016.
 */

public class FragmentUsuarioDetalle extends Fragment {

    private Double userLatitude;
    private Double userLongitude;


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

        final ImageView imagenUsuario = (ImageView) viewADevolverInflado.findViewById(R.id.userdetalle_imageView_imagenUsuario);
        final TextView nombreedadUsuario = (TextView) viewADevolverInflado.findViewById(R.id.userdetalle_nombreyEdad);
        final TextView descUsuario = (TextView) viewADevolverInflado.findViewById(R.id.userdetalle_descripcion);
        final TextView distanciaUsuario = (TextView) viewADevolverInflado.findViewById(R.id.userdetalle_distancia);


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

        database.getReference("users").child(idUsuarioSeleccionado).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Usuario usuarioSeleccionado = dataSnapshot.getValue(Usuario.class);
                nombreedadUsuario.setText(usuarioSeleccionado.getNombre() + ", " + usuarioSeleccionado.getEdad());
                descUsuario.setText(usuarioSeleccionado.getDescripcion());
                final Location userLastLocation = new Location("");
                userLastLocation.setLongitude(usuarioSeleccionado.getLongitudeCoordinate());
                userLastLocation.setLatitude(usuarioSeleccionado.getLatitudeCoordinate());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");

                storageRef.child(usuarioSeleccionado.getId() + "_profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getActivity()).load(uri.toString()).into(imagenUsuario);
                        // Got the download URL for 'users/me/profile.png'
                        // Pass it to Picasso to download, show in ImageView and caching

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Picasso.with(getActivity()).load(usuarioSeleccionado.getFoto()).into(imagenUsuario);

                        // Handle any errors
                    }
                });

                //busca el usuario de firebase
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    userLatitude = dataSnapshot.child("latitudeCoordinate").getValue(Double.class);
                                    userLongitude = dataSnapshot.child("longitudeCoordinate").getValue(Double.class);
                                    Location myLocation = new Location("");
                                    myLocation.setLongitude(userLongitude);
                                    myLocation.setLatitude(userLatitude);
                                    float distanciaEntreUsuarios = myLocation.distanceTo(userLastLocation);
                                    distanciaUsuario.setText("ultimo Login a: " + distanciaEntreUsuarios + " metros");

                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
