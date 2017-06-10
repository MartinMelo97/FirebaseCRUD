package com.actosoftcommunity.firebaseautentication;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by martinmelo on 5/13/17.
 */

public class OtroDialogFragment extends android.support.v4.app.DialogFragment{

    public interface OtroDialogListener
    {
        public void check(String correo, String contra);
    }

    OtroDialogListener mListener;


    @Override
    public void onAttach(Context context)
    {
            super.onAttach(context);
            try
            {
                mListener = (OtroDialogListener) context;
            }
            catch(ClassCastException e) {
                throw new ClassCastException((context.toString() + "must implement LoginDialogListener"));
            }
    }

    EditText ETCorreo, ETPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(dialogView);

        ETCorreo = (EditText) dialogView.findViewById(R.id.Email);
        ETPassword = (EditText) dialogView.findViewById(R.id.Contra);

        builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String correo = ETCorreo.getText().toString();
                String pass = ETPassword.getText().toString();
                buttonClicked(correo, pass);
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OtroDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void buttonClicked(String correo, String contra)
    {
        mListener.check(correo, contra);
    }
}
