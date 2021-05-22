package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AnnouncemntActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapterAnnouncemnet postAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<AnnouncementPC> announcementList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcemnt);

        progressBar=findViewById(R.id.progressBar);
        database= FirebaseDatabase.getInstance();
        reference=database.getReference().child("Announcement");
        recyclerView = findViewById(R.id.announcementRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // show new post first, for this load from last

        // set Layout to recycler view
        announcementList=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapterAnnouncemnet((ArrayList<AnnouncementPC>) announcementList,this);
        recyclerView.setAdapter(postAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AnnouncementPC announcementPC=dataSnapshot.getValue(AnnouncementPC.class);
                announcementList.add(announcementPC);
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
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
