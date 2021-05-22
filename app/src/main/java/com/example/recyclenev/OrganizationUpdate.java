package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrganizationUpdate extends AppCompatActivity {

    ImageButton upadte;
    EditText Date, tier, ceo, domain, contact, email, CocCeo, CulSec, description;
    FirebaseAuth auth;
    FirebaseUser fuser;
    DatabaseReference fdatabase;
    CircleImageView uploadLogo, updateLogo;
    TextView organizationName;
    String imageUri,uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_update);

        //  Date = (EditText) findViewById(R.id.etDate);
        tier = (EditText) findViewById(R.id.etTier);
        domain = (EditText) findViewById(R.id.etDomain);
        contact = (EditText) findViewById(R.id.etContact);
        CocCeo = (EditText) findViewById(R.id.etm1);
        CulSec = (EditText) findViewById(R.id.etm2);
        description = (EditText) findViewById(R.id.etDescription);
        //  email = (EditText) findViewById(R.id.etEmail);
        ceo = (EditText) findViewById(R.id.etCEO);
        auth = FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();
        uploadLogo = (CircleImageView) findViewById(R.id.updateLogo);
        updateLogo = (CircleImageView) findViewById(R.id.updateLogo);
        organizationName = (TextView) findViewById(R.id.OrganizationName);


        Intent i= getIntent();
        organizationName.setText(i.getStringExtra("name1"));

        uploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizationUpdate.this, Logo.class);
                startActivityForResult(intent, 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            imageUri = data.getStringExtra("uri");
            Glide.with(OrganizationUpdate.this).asBitmap().load(imageUri).into(updateLogo);
            Toast.makeText(this, "Logo updation successful", Toast.LENGTH_SHORT).show();

        }

    }

    public void click(View v) {
        Intent i = getIntent();
        String name = i.getStringExtra("name1");
        String password = i.getStringExtra("password1");
        String uid = FirebaseAuth.getInstance().getUid();
        fdatabase = FirebaseDatabase.getInstance().getReference("Organisation").child("Detail").child(uid);
        String[] s = {CocCeo.getText().toString(), CulSec.getText().toString()};

        Update updateObj = new Update(tier.getText().toString(), domain.getText().toString(), contact.getText().toString(), ceo.getText().toString(), CocCeo.getText().toString(), CulSec.getText().toString(), description.getText().toString(), name, uid, imageUri);

        fdatabase.setValue(updateObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OrganizationUpdate.this, "updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrganizationUpdate.this,MainActivity.class));
                } else {
                    Toast.makeText(OrganizationUpdate.this, "something went wong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
