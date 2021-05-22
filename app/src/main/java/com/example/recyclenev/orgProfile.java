package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class orgProfile extends AppCompatActivity {

    TextView name;
    EditText ceo,startDate,tier;
    FirebaseAuth auth;
    ImageView dp;
    Button update;
    DatabaseReference Datareference, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);

        name=(TextView)findViewById(R.id.name);
        ceo=(EditText)findViewById(R.id.ceoEdit);
        startDate=(EditText)findViewById(R.id.startEdit);
        tier=(EditText)findViewById(R.id.tierEdit);
        auth = FirebaseAuth.getInstance();
        dp=(ImageView)findViewById(R.id.Dp);
        update=(Button)findViewById(R.id.update);

        final Intent i=getIntent();
        name.setText(i.getStringExtra("username"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgName=name.getText().toString();
                String orgEmail= i.getStringExtra("orgEmail");
                String orgCeo=ceo.getText().toString();
                String orgStart= startDate.getText().toString();
                String orgTier= tier.getText().toString();
                String uid=i.getStringExtra("uid");

                Datareference= FirebaseDatabase.getInstance().getReference("Organization").child(uid);
                d= FirebaseDatabase.getInstance().getReference("Organization").child(uid);
                HashMap<String, String> hashMap=new HashMap<>();
                hashMap.put("ceo",orgCeo);
                hashMap.put("startDate",orgStart);
                hashMap.put("tier",orgTier);
                hashMap.put("imageurl","default");

                Datareference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(orgProfile.this, "Update done successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(orgProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
