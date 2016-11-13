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
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;

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
        unAdapterUsuarios = new AdapterRecycleUsuarios(getActivity(),new ArrayList<Usuario>());
        recyclerViewUsuario.setAdapter(unAdapterUsuarios);
        return unaVistaADevolver;



    }
}
