package com.example.nicolaslopezf.entregablefinal.view;

/**
 * Created by santiagoiraola on 12/4/16.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        final ImageView imagenUsuario = (ImageView) findViewById(R.id.user_imageView_imagenUsuario);
        final EditText nombreUsuario = (EditText) findViewById(R.id.user_editText_nombre);
        final EditText edadUsuario = (EditText) findViewById(R.id.user_editText_edad);
        final EditText descUsuario = (EditText) findViewById(R.id.user_editText_desc);
        final EditText mailUsuario = (EditText) findViewById(R.id.user_editText_mail);
        Button saveChanges = (Button) findViewById(R.id.user_saveChanges);

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        final FirebaseUser usuarioLogeado = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String id = usuarioLogeado.getUid();

        database.getReference("users").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            Picasso.with(UserSettingsActivity.this).load(usuario.getFoto()).into(imagenUsuario);
                            edadUsuario.setText(usuario.getEdad());
                            nombreUsuario.setText(usuario.getNombre());
                            descUsuario.setText(usuario.getDescripcion());
                            mailUsuario.setText(usuario.getEmail());

                        }
                        else {

                        }

                    }




                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final String id = usuarioLogeado.getUid();
                database.getReference("users").child(id).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    //el anterior por si es un gato
                                    Usuario usuario = new Usuario(usuarioLogeado.getEmail(), id);
                                    usuario.setFoto(usuarioLogeado.getPhotoUrl().toString());
                                    usuario.setNombre(usuarioLogeado.getDisplayName());

                                    //los cambios
                                    usuario.setEdad(edadUsuario.getText().toString());
                                    usuario.setNombre(nombreUsuario.getText().toString());
                                    usuario.setEmail(mailUsuario.getText().toString());
                                    usuario.setDescripcion(descUsuario.getText().toString());

                                    dataSnapshot.child("nombre").getRef().setValue(usuario.getNombre());
                                    dataSnapshot.child("edad").getRef().setValue(usuario.getEdad());
                                    dataSnapshot.child("email").getRef().setValue(usuario.getEmail());
                                    dataSnapshot.child("desc").getRef().setValue(usuario.getDescripcion());


                                }
                                else {

                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }
        });

    }
}