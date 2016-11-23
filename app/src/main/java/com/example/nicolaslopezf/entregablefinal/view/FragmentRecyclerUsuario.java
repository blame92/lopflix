package com.example.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class FragmentRecyclerUsuario extends Fragment {

    private RecyclerView recyclerViewUsuario;
    private AdapterRecycleUsuarios unAdapterUsuarios;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);
        recyclerViewUsuario= (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewUsuario.setLayoutManager(linearLayoutManager);
        final ArrayList<Usuario> usuariosDelAdapter = new ArrayList<>();
        unAdapterUsuarios = new AdapterRecycleUsuarios(getActivity(),usuariosDelAdapter,new ListenerUsuarios());
        recyclerViewUsuario.setAdapter(unAdapterUsuarios);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirebaseUser usuarioLogeado = mAuth.getCurrentUser();
        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userDataSnapshot : dataSnapshot.getChildren()){
                    Usuario usuario = userDataSnapshot.getValue(Usuario.class);
                    usuariosDelAdapter.add(usuario);
                    unAdapterUsuarios.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return unaVistaADevolver;

    }

    public class ListenerUsuarios implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewUsuario.getChildAdapterPosition(view);
            Usuario usuarioTocado = unAdapterUsuarios.getUsuarioAtPosition(posicionTocada);

            ComunicadorFragmentActivity unComunicador = (ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEsteUsuario(usuarioTocado);

        }
    }
    public interface ComunicadorFragmentActivity{
        public void clickearonEsteUsuario(Usuario usuarioClick);

    }

}
