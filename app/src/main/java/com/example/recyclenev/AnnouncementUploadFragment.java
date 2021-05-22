package com.example.recyclenev;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceGroup;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AnnouncementUploadFragment extends Fragment {

    EditText editTextTitle,editTextDescription;
    Button buttonUpload;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_announcement_upload, container, false);

        editTextTitle=v.findViewById(R.id.editTextAnnTitle);
        editTextDescription=v.findViewById(R.id.editTextAnnDescription);
        buttonUpload=v.findViewById(R.id.buttonAnnUpload);
        progressBar=v.findViewById(R.id.progressBar);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Announcement").push();
        final String user= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String title=editTextTitle.getText().toString();
                String username=user.substring(0,user.length()-10);
                String description=editTextDescription.getText().toString();

                if(TextUtils.isEmpty(title)){
                    editTextTitle.setError("Enter title");
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    editTextDescription.setError("Enter description");
                    return;
                }

                AnnouncementPC announcementPC=new AnnouncementPC();
                announcementPC.setTitle(title);
                announcementPC.setUserName(username);
                announcementPC.setDescription(description);
                reference.setValue(announcementPC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Announcement Uploaded", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().remove(AnnouncementUploadFragment.this).commit();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        return v;
    }

}
