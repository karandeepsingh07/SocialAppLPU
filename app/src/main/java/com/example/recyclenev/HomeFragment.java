package com.example.recyclenev;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<PostDetailPC> postDetailList;
    FirebaseAuth auth;
    String uid;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_home,container,false);



        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        uid=user.getUid();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Posts").child("Details");
        //recycleView and properties
        recyclerView = v.findViewById(R.id.postRecycleView);
        progressBar = v.findViewById(R.id.progressBar);
       LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // show new post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        // set Layout to recycler view
        postDetailList=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapter((ArrayList<PostDetailPC>) postDetailList,getContext());
        recyclerView.setAdapter(postAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot orgSnapshot : dataSnapshot.getChildren()) {
                    PostDetailPC postDetailPC=orgSnapshot.getValue(PostDetailPC.class);
                    postDetailList.add(postDetailPC);
                    progressBar.setVisibility(View.GONE);
                    postAdapter.notifyDataSetChanged();
                }
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

        /*for (DataSnapshot orgSnapshot : dataSnapshot.getChildren()) {
                    PostDetailPC postDetailPC = orgSnapshot.getValue(PostDetailPC.class);
                    postDetailList.add(postDetailPC);
                }
                for (int i=0;i<postDetailList.size();i++)
                {

                }*/


        return v;
    }

}
