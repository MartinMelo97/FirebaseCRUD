package com.actosoftcommunity.firebaseautentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends FragmentActivity implements
    OtroDialogFragment.OtroDialogListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button BtnLogin, BtnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Putoo", "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent intento = new Intent(getApplicationContext(), CerrarSesion.class);
                    startActivity(intento);
                    finish();
                } else {
                    // User is signed out
                    Log.d("putito", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        BtnLogin = (Button) findViewById(R.id.BtnLogin);
        BtnSignIn = (Button) findViewById(R.id.BtnRegistro);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtroDialogFragment dialog = new OtroDialogFragment();
                dialog.show(getSupportFragmentManager(), "OtroDialogManager");
            }
        });

        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Registrate", Toast.LENGTH_SHORT).show();
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

    public void check(String correo, String contra)
    {
        //Toast.makeText(this, ""+correo, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+contra, Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Putito", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("putito", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "No existes prro", Toast.LENGTH_SHORT).show();
                            }

                        // ...
                    }
                });
    }
}
