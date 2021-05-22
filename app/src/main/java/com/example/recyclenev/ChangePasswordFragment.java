package com.example.recyclenev;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    EditText oldPass,newPass;
    Button buttonChangePass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_change_password, container, false);;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        oldPass=v.findViewById(R.id.editOldPass);
        newPass=v.findViewById(R.id.editpassword);
        buttonChangePass=v.findViewById(R.id.btLoginL);
        Toast.makeText(getContext(), ""+user.getEmail(), Toast.LENGTH_SHORT).show();

        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), oldPass.getText().toString());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                                getFragmentManager().beginTransaction().remove(ChangePasswordFragment.this).commit();
                                            } else {
                                                Toast.makeText(getContext(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                                getFragmentManager().beginTransaction().remove(ChangePasswordFragment.this).commit();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return v;
    }

}
