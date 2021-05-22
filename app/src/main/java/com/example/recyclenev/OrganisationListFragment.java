package com.example.recyclenev;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class OrganisationListFragment extends Fragment {
    RecyclerView recyclerView;
    PostAdapterOrgList postAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<OrganisationPC> organisationLIist;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_organisation_list, container, false);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference().child("Organisation").child("Detail");
        recyclerView = v.findViewById(R.id.orgRecycleView);
        progressBar=v.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // show new post first, for this load from last

        // set Layout to recycler view
        organisationLIist=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapterOrgList((ArrayList<OrganisationPC>) organisationLIist,getContext());
        recyclerView.setAdapter(postAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                progressBar.setVisibility(View.VISIBLE);
                OrganisationPC postDetailPC=dataSnapshot.getValue(OrganisationPC.class);
                organisationLIist.add(postDetailPC);
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

        return v;
    }


}
