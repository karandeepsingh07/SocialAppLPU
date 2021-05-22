package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapterComment postAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<CommentPC> commentLiist;
    EditText editTextComment;
    Button buttonComment;
    String username,ppuid,puid,uid,userDp;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        ppuid=getIntent().getStringExtra("upid");
        puid=getIntent().getStringExtra("pid");

        /*String postId=getIntent().getStringExtra("postId");*/
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        username=user.getEmail();
        uid=user.getUid();
        username=username.substring(0,username.length()-8);


        editTextComment=findViewById(R.id.editTextComment);
        buttonComment=findViewById(R.id.buttonComment);
        progressBar=findViewById(R.id.progressBar);
        database= FirebaseDatabase.getInstance();
        reference=database.getReference();
        recyclerView = findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // show new post first, for this load from last

        // set Layout to recycler view
        commentLiist=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapterComment((ArrayList<CommentPC>) commentLiist,this);
        recyclerView.setAdapter(postAdapter);

        reference.child("Posts").child("Comments").child(ppuid).child(puid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CommentPC commentPC=dataSnapshot.getValue(CommentPC.class);
                commentLiist.add(commentPC);
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

        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentPC commentPC=new CommentPC();
                commentPC.setComment(editTextComment.getText().toString());
                commentPC.setUsername(username);
                reference.child("Posts").child("Comments").child(ppuid).child(puid).push().setValue(commentPC);
                editTextComment.setText("");
            }
        });
    }
}
