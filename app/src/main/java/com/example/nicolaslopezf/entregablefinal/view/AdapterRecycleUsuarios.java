package com.example.nicolaslopezf.entregablefinal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class AdapterRecycleUsuarios  extends RecyclerView.Adapter{
    private Context unContexto;
    private ArrayList<Usuario> listaDeUsuarios;
    private View.OnClickListener listener;

    public AdapterRecycleUsuarios(Context unContexto, ArrayList<Usuario> listaDeUsuarios, View.OnClickListener listener) {
        this.unContexto = unContexto;
        this.listener = listener;
        this.listaDeUsuarios = listaDeUsuarios;

    }

    public AdapterRecycleUsuarios(Context unContexto, ArrayList<Usuario> listaDeUsuarios) {
        this.unContexto = unContexto;
//        listaDeUsuarios.add(new Usuario("Blamme1993", "24","Rom-Com"));
//        listaDeUsuarios.add(new Usuario("Santi Iraola", "24","Accion"));
//        listaDeUsuarios.add(new Usuario("Fezilio", "24","porno"));
//        listaDeUsuarios.add(new Usuario("McLopi", "24","Comedia"));
//        listaDeUsuarios.add(new Usuario("Blamme1992", "24","Rom-Com"));

        this.listaDeUsuarios = listaDeUsuarios;    }

    public ArrayList<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    public void setListaDeUsuarios(ArrayList<Usuario> listaDeUsuarios) {
        this.listaDeUsuarios = listaDeUsuarios;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(unContexto);

        View vistaADevolver = inflater.inflate(R.layout.fragment_pelicula_item, parent, false);
        vistaADevolver.setOnClickListener(listener);
        AdapterRecycleUsuarios.UsuarioViewHolder unViewHolderUsuario = new AdapterRecycleUsuarios.UsuarioViewHolder(vistaADevolver);
        return unViewHolderUsuario;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Usuario UserABindear = listaDeUsuarios.get(position);
        UsuarioViewHolder unUsuarioViewHolder = (AdapterRecycleUsuarios.UsuarioViewHolder) holder;
        unUsuarioViewHolder.bindReceta(UserABindear);





    }

    @Override
    public int getItemCount() {
        return listaDeUsuarios.size();
    }

    private class UsuarioViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagenUsuario;
        private TextView nombreUsuario;
        private TextView edadUsuario;
        private TextView generoFavUsuario;

        public UsuarioViewHolder(View itemView){
            super(itemView);
            imagenUsuario = (ImageView)itemView.findViewById(R.id.ivPelicula);
            nombreUsuario = (TextView)itemView.findViewById(R.id.tvNombrePelicula);
            edadUsuario = (TextView) itemView.findViewById(R.id.tvDateOfRelease);
            generoFavUsuario = (TextView) itemView.findViewById(R.id.tvDirector);


        }
        public void bindReceta(Usuario unUsuario){
            imagenUsuario.setBackgroundResource(R.drawable.lopo_icon_two);
//            nombreUsuario.setText(unUsuario.getUserName());
//            edadUsuario.setText(unUsuario.getAge() + " anios");
//            generoFavUsuario.setText(unUsuario.getFavGenre());
        }
    }
}



