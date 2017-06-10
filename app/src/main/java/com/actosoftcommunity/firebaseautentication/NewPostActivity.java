package com.actosoftcommunity.firebaseautentication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class NewPostActivity extends AppCompatActivity {

    private EditText ETTitulo, ETContenido;
    private ImageButton ImageCont;
    private Button BtnSubir;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private String email, titulo, cuerpo, uid;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    email = user.getEmail();
                    uid = user.getUid();
                } else {
                    // User is signed out
                    Intent intentito = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentito);
                }
                // ...
            }
        };

        ETTitulo = (EditText) findViewById(R.id.ETTitulo);
        ETContenido = (EditText) findViewById(R.id.ETContenido);
        ImageCont = (ImageButton) findViewById(R.id.ImagenContainer);
        BtnSubir = (Button) findViewById(R.id.BtnUpload);

        ImageCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }
        });

        BtnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            mImageUri = data.getData();
            ImageCont.setImageURI(mImageUri);
        }
    }

    private void uploadToFirebase(){
        titulo = ETTitulo.getText().toString().trim();
        cuerpo = ETContenido.getText().toString().trim();
        email = email.trim();

        Uri file = Uri.fromFile(new File(mImageUri.getLastPathSegment()));
        StorageReference Storageref = mStorageRef.child("posts/images").child(mImageUri.getLastPathSegment());
        Storageref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String urlImage = downloadUrl.toString().trim();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Posts");
                String id = myRef.push().getKey();
                Post post = new Post(titulo, cuerpo, email, urlImage);
                myRef.child(id).setValue(post);
                Toast.makeText(NewPostActivity.this, "Tu post se ha agregado con exito!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
