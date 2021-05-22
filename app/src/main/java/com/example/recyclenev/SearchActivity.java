package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<PostDetailPC> postDetailList;
    Spinner spinnerCategory;
    String category;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Posts").child("Details");

        recyclerView = findViewById(R.id.recyclerViewSearch);
        spinnerCategory=findViewById(R.id.spinnerSearch);
        progressBar=findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        postDetailList=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapter((ArrayList<PostDetailPC>) postDetailList,this);
        recyclerView.setAdapter(postAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerCategory.getSelectedItemPosition()==0){
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                postDetailList.clear();
                postAdapter.notifyDataSetChanged();
                category=spinnerCategory.getSelectedItem().toString();
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        for (DataSnapshot orgSnapshot : dataSnapshot.getChildren()) {
                            PostDetailPC postDetailPC=orgSnapshot.getValue(PostDetailPC.class);
                            if(postDetailPC.getCategory().equals(category)) {
                                postDetailList.add(postDetailPC);
                                postAdapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
