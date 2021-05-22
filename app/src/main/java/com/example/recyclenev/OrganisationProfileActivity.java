package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganisationProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapterOrgPostList postAdapter;
    List<PostDetailPC> orgPostList;
    FirebaseDatabase database;
    DatabaseReference reference;
    String uid,orgName,orgImageUrl;
    TextView textViewOrgName;
    CircleImageView imageViewOrg;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView editTextName,editTextAbout,editTextCEO,editTextCoCEO,editTextCulSec,editTextContact;
    String name,about,ceo,coCeo,culSec,contact,imageViewUrl,ceoImageUrl,coCeoImageUrl,culSecImageUrl;
    CircleImageView imageViewCeo,imageViewCoCeo,imageViewCulSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_profile);

        Bundle bundle = getIntent().getExtras();
        uid= bundle.getString("Udi");

        editTextName=findViewById(R.id.editTextOrgTitle);
        editTextAbout=findViewById(R.id.editTextAboutOrg);
        editTextCEO=findViewById(R.id.editTextCeoOrg);
        editTextCoCEO=findViewById(R.id.editTextCoCeoOrg);
        editTextCulSec=findViewById(R.id.editTextCulSecOrg);
        editTextContact=findViewById(R.id.editTextContactOrg);
        imageViewCeo=findViewById(R.id.imageViewCEO);
        imageViewCoCeo=findViewById(R.id.imageViewCoCEO);
        imageViewCulSec=findViewById(R.id.imageViewCulSec);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();

        recyclerView = findViewById(R.id.orgRecycleView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);

        // show new post first, for this load from last
        layoutManager.setReverseLayout(true);

        // set Layout to recycler view
        orgPostList=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapterOrgPostList((ArrayList<PostDetailPC>) orgPostList,this);
        recyclerView.setAdapter(postAdapter);

        textViewOrgName=findViewById(R.id.textViewOrganizationName);
        imageViewOrg=findViewById(R.id.imageViewOrganisation);

        reference.child("Organisation").child("Detail").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrganisationPC organisationPC=dataSnapshot.getValue(OrganisationPC.class);

                orgName=organisationPC.getTitle();
                orgImageUrl=organisationPC.getImageUrl();
                name=organisationPC.getTitle();
                about=organisationPC.getAbout();
                ceo=organisationPC.getCeo();
                coCeo=organisationPC.getCoCeo();
                culSec=organisationPC.getCulSec();
                contact=organisationPC.getContact();
                imageViewUrl=organisationPC.getImageUrl();
                ceoImageUrl=organisationPC.getImageUrlCeo();
                coCeoImageUrl=organisationPC.getImageUrlCoCeo();
                culSecImageUrl=organisationPC.getImageUrlCulsec();



                editTextAbout.setText(about);
                editTextCEO.setText(ceo);
                editTextCoCEO.setText(coCeo);
                editTextCulSec.setText(culSec);
                editTextContact.setText(contact);
                Glide.with(OrganisationProfileActivity.this).load(orgImageUrl).into(imageViewOrg);
                textViewOrgName.setText(orgName);
                Glide.with(OrganisationProfileActivity.this).load(ceoImageUrl).into(imageViewCeo);
                Glide.with(OrganisationProfileActivity.this).load(coCeoImageUrl).into(imageViewCoCeo);
                Glide.with(OrganisationProfileActivity.this).load(culSecImageUrl).into(imageViewCulSec);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Posts").child("Details").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostDetailPC postDetailPC=dataSnapshot.getValue(PostDetailPC.class);
                orgPostList.add(postDetailPC);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
