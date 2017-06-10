package com.actosoftcommunity.firebaseautentication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CerrarSesion extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    //Button BtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_sesion);

       // BtnLogout = (Button) findViewById(R.id.BtnLogout);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("lo que quiera", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("log que quiera", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

       fab = (FloatingActionButton) findViewById(R.id.NewPost);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intento = new Intent(getApplicationContext(), NewPostActivity.class);
               startActivity(intento);
           }
       });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.item_post,
                PostViewHolder.class,
                mDatabase)
        {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
                viewHolder.setTitulo(model.getTitulo());
                viewHolder.setContenido(model.getContenido());
                viewHolder.setAutor(model.getAutor());
                viewHolder.setImage(getApplicationContext(), model.getImagen());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitulo(String titulo)
        {
            TextView TVTitulo = (TextView) mView.findViewById(R.id.TituloPost);
            TVTitulo.setText(titulo);
        }

        public void setContenido(String contenido){
            TextView TVContenido = (TextView) mView.findViewById(R.id.CuerpoPost);
            TVContenido.setText(contenido);
        }

        public void setAutor(String autor)
        {
            TextView TVAutor = (TextView) mView.findViewById(R.id.AutorTitulo);
            TVAutor.setText(autor);
        }

        public void setImage(Context mCon, String url)
        {
            ImageView imagePost = (ImageView) mView.findViewById(R.id.imagenPost);
            Picasso.with(mCon).load(url).into(imagePost);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
