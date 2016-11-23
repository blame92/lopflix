package com.example.nicolaslopezf.entregablefinal.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.example.nicolaslopezf.entregablefinal.dao.PeliculaDAO;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDBTrailerContainer;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.example.nicolaslopezf.entregablefinal.model.WrapperPeliculas;
import com.example.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.example.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.example.nicolaslopezf.entregablefinal.view.YouTube.YouTubeFragment;
import com.example.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.AdapterRecyclerSoloImagen;
import com.example.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;

/**
 * Created by mora on 24/10/2016.
 */

public class FragmentPeliculaDetallada extends Fragment {


    private Pelicula pelicula;
    private RecyclerView recyclerViewPeliculas;
    private AdapterRecyclerSoloImagen unAdapterPelicula;
    private FragmentManager unFragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Bundle bundleRecibido = getArguments();

        final View viewADevolverInflado = inflater.inflate(R.layout.fragment_pelicula_detalle, container, false);

        recyclerViewPeliculas = (RecyclerView) viewADevolverInflado.findViewById(R.id.fragmentpeliculadetalle_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculas.setLayoutManager(linearLayoutManager);

        final YouTubeFragment youTubeFragment = new YouTubeFragment();


        recyclerViewPeliculas = (RecyclerView) viewADevolverInflado.findViewById(R.id.fragmentpeliculadetalle_recycleView);
        final TextView textViewNombre = (TextView) viewADevolverInflado.findViewById(R.id.textNombrePeliculaItem);
        final ImageView imageViewFoto = (ImageView) viewADevolverInflado.findViewById(R.id.imageViewPeliculaItem);
        final TextView textViewActores = (TextView) viewADevolverInflado.findViewById(R.id.textActoresPeliculaItem);
        final TextView textViewDescripcion = (TextView) viewADevolverInflado.findViewById(R.id.textDescripcionPeliculaItem);
        final CheckBox checkBoxRating = (CheckBox) viewADevolverInflado.findViewById(R.id.textScorePeliculaItem);
        final FloatingActionButton flotingActionB = (FloatingActionButton) viewADevolverInflado.findViewById(R.id.buttonFav);

        final PeliculaController peliculaController = new PeliculaController();


        final String imdbID = bundleRecibido.getString("imdbID");

        final String tmdbID = bundleRecibido.getString("tmdbID");

        // toDo cambiar para que lo haga el controller
        PeliculaDAO peliculaDAO = new PeliculaDAO(getActivity());
        peliculaDAO.flasehadaCreadoraDeAsync(new Pelicula(), "http://www.omdbapi.com/?i=" + imdbID, new ResultListener<Object>() {

            @Override
            public void finish(Object resultado) {
                pelicula = (Pelicula) resultado;
                flotingActionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PeliculaController peliculaController = new PeliculaController();
                        peliculaController.changeFavoriteStatus(getActivity(),pelicula,imdbID,tmdbID);

                        if(peliculaController.esFavorita(pelicula,getActivity())){

                            // codigo que agrega una pelicula a la lista de favoritos de firbase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = database.getReference("users");
                            FirebaseAuth mAuth;
                            mAuth  = FirebaseAuth.getInstance();
                            final FirebaseUser user = mAuth.getCurrentUser();
                            ArrayList<String> container = new ArrayList<>();
                            container.add(pelicula.getImdbID());
                            container.add(pelicula.getTitle());
                            databaseReference.child(user.getUid()).child("watchlist").child(pelicula.getImdbID()).setValue(pelicula);


                            flotingActionB.setImageResource(R.mipmap.ic_favorite_full);
                            flotingActionB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                        }else{
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            FirebaseAuth mAuth;
                            mAuth  = FirebaseAuth.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                            database.getReference("users").child(user.getUid()).child("watchlist").child(pelicula.getImdbID()).equalTo(pelicula.getImdbID()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            dataSnapshot.getRef().
                                                    setValue(null);
                                        }


                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                                        }
                                    });
                            flotingActionB.setImageResource(R.mipmap.ic_favorite);
                            flotingActionB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundColor)));
                        }

                        Toast.makeText(getActivity(), pelicula.getTitle()+  " fue agregado a tu lista de favorito", Toast.LENGTH_SHORT).show();
                    }
                });
                if(peliculaController.esFavorita(pelicula,getActivity())){
                    flotingActionB.setImageResource(R.mipmap.ic_favorite_full);
                }else{
                    flotingActionB.setImageResource(R.mipmap.ic_favorite);
                    flotingActionB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundColor)));
                }
                textViewNombre.setText(pelicula.getTitle() );
                textViewActores.setText("Actors: \n" + pelicula.getActors());
                textViewDescripcion.setText(pelicula.getPlot());
                checkBoxRating.setText(pelicula.getImdbRating() + "/10");
                Picasso.with(getContext()).load(pelicula.getPoster()).placeholder(R.mipmap.ic_launcher).into(imageViewFoto);

//                ArrayList<Pelicula> peliculasDelRecycle = peliculasController.damePeliculasPorGenero(genero,getContext());
//                peliculasDelRecycle.remove(pelicula);
//                long seed = System.nanoTime();
//                Collections.shuffle(peliculasDelRecycle, new Random(seed));

                // TODO: 11/13/16 AGREGAR LISTENER AL ADAPTER

                //recyclerViewPeliculas.setAdapter(unAdapterPelicula);

            }
        },getActivity());

        unAdapterPelicula = new AdapterRecyclerSoloImagen(getActivity());
        unAdapterPelicula.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculas,unAdapterPelicula));
        peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getSimilarMovies(tmdbID, TMDBHelper.language_ENGLISH, 1), getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB peliculasSimilares = (ContainerMovieDB) resultado;
                unAdapterPelicula.setListaDePeliculas(peliculasSimilares.getResult());
                unAdapterPelicula.notifyDataSetChanged();
            }
        });

        recyclerViewPeliculas.setAdapter(unAdapterPelicula);

//        peliculaController.obtenerPeliculaPorID(getActivity(), imdbID, new ResultListener<Pelicula>() {
//            @Override
//            public void finish(Pelicula resultado) {
//                textViewNombre.setText(resultado.getTitle());
//                Picasso.with(getContext()).load(resultado.getPoster()).placeholder(R.mipmap.ic_launcher).into(imageViewFoto);
//            }
//        });

        peliculaController.obtenerTrailerDePeliculaTMDB(getActivity(), tmdbID, new ResultListener() {
            @Override
            public void finish(Object resultado) {
                MovieDBTrailerContainer movieDBTrailerContainer = (MovieDBTrailerContainer) resultado;
                Log.d("trailer",movieDBTrailerContainer.toString());
                String url;
                if(movieDBTrailerContainer.getTrailerArrayList().size() > 0){
                    url = movieDBTrailerContainer.getTrailerArrayList().get(0).getTrailerUrl();
                }
                else {
                    url = "zX5XasRcGBg";
                }

                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                youTubeFragment.setArguments(bundle);

                unFragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPeliculaDetalle_alojadorDeYoutube,youTubeFragment);
                fragmentTransaction.commit();

            }
        });


        return viewADevolverInflado;

    }
    public class ListenerPeliculasSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclerSoloImagen adapterAUsar;

        public ListenerPeliculasSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclerSoloImagen adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            MovieDB peliculaTocada = adapterAUsar.getPeliculaAtPosition(posicionTocada);
            Log.d("imagen",peliculaTocada.getPoster());

            FragmentRecyclerSoloImagen.ComunicadorFragmentActivity unComunicador = (FragmentRecyclerSoloImagen.ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaPelicula(peliculaTocada);


        }
    }


    public interface ComunicadorFragmentActivity{
        void clickearonEstaPelicula(MovieDB peliculaClickeada);

    }
}
