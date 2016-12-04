package com.example.nicolaslopezf.entregablefinal.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.example.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.example.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.internal.TwitterApi;

import java.util.zip.Inflater;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements FragmentRecyclerSoloImagen.ComunicadorFragmentActivity,FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity,FragmentRecyclerUsuario.ComunicadorFragmentActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "o0AwMxuktney6F0OxNZXhup35";
    private static final String TWITTER_SECRET = "bkItOFtZmpTPmBDWP3zEylH2iYeOUL1eLy6a3AVaV444eXra0w";


    DrawerLayout drawerLayout;
    private FragmentManager unFragmentManager;
    PeliculaController peliculaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        peliculaController = new PeliculaController();
        peliculaController.obtenerPeliculasPorGenero("sci-fi", this, new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB containerMovieDB = (ContainerMovieDB) resultado;
            }
        });



        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        unFragmentManager = getSupportFragmentManager();


        FragmentTransaction unaTransaction = unFragmentManager.beginTransaction();
        unaTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentViewPager);
        unaTransaction.commit();



        //SETEAR NAVIGATION VIEW Y SU LISTENER
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationViewPorGeneroListener());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

//         agrega la foto del usuario a el circle image view
//        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.navigationheader_circleimage);
//        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
//        FirebaseUser usuarioAAgregar = mAuth.getCurrentUser();
//        Picasso.with(this).load(usuarioAAgregar.getPhotoUrl().toString()).into(circleImageView);


    }


    private class NavigationViewPorGeneroListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.toString().equals("Settings")){
                Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                unFragmentManager = getSupportFragmentManager();
                FragmentViewPager fragmentViewPager = new FragmentViewPager();
                FragmentRecyclerSoloImagen fragmentRecyclerSoloImagen = new FragmentRecyclerSoloImagen();
                Bundle bundle = new Bundle();
                bundle.putString("genero", item.toString());
                fragmentRecyclerSoloImagen.setArguments(bundle);

                FragmentTransaction unaTransaction = unFragmentManager.beginTransaction();
                unaTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentRecyclerSoloImagen).addToBackStack(null);
                unaTransaction.commit();
            }


            drawerLayout.closeDrawers();
            return true;
        }
    }
    public void clickearonEstaPelicula(MovieDB peliculaClickeada) {

        peliculaController.obtenerPeliculaTMDB(peliculaClickeada.getId(), this, new ResultListener() {
            @Override
            public void finish(Object resultado) {

                FragmentPeliculaDetallada fragmentPeliculaDetallada = new FragmentPeliculaDetallada();
                MovieDB movieDB = (MovieDB) resultado;
                Log.d("movieDB",movieDB.toString());
                Bundle unBundle = new Bundle();
                unBundle.putString("imdbID", movieDB.getImdbID());
                unBundle.putString("tmdbID",movieDB.getId());
                fragmentPeliculaDetallada.setArguments(unBundle);

                FragmentTransaction unaTransaccion = unFragmentManager.beginTransaction();
                unaTransaccion.replace(R.id.acaVaElFragmentPelicula, fragmentPeliculaDetallada).addToBackStack("backButton");
                unaTransaccion.commit();
            }
        });
    }    public void clickearonEsteUsuario(Usuario usuarioClickeado) {


                FragmentUsuarioDetalle fragmentUsuarioDetalle = new FragmentUsuarioDetalle();

                Bundle unBundle = new Bundle();
                unBundle.putString("idClick", usuarioClickeado.getId());

                fragmentUsuarioDetalle.setArguments(unBundle);

                FragmentTransaction unaTransaccion = unFragmentManager.beginTransaction();
                unaTransaccion.replace(R.id.acaVaElFragmentPelicula, fragmentUsuarioDetalle).addToBackStack("backButton");
                unaTransaccion.commit();
            }



    @Override
    public void clickearonEstaPeliculaDeFavoritos(Pelicula peliculaClickeada) {

        FragmentPeliculaDetallada fragmentPeliculaDetallada = new FragmentPeliculaDetallada();
        Bundle unBundle = new Bundle();
        unBundle.putString("imdbID",peliculaClickeada.getImdbID());
        //unBundle.putString("tmdbID",peliculaController.obtenerTMDBidDeBaseDeDatos(this,peliculaClickeada.getImdbID()));

        fragmentPeliculaDetallada.setArguments(unBundle);

        FragmentTransaction fragmentTransaction = unFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentPeliculaDetallada).addToBackStack("backButton");
        fragmentTransaction.commit();
    }
}


