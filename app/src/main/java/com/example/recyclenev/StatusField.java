package com.example.recyclenev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StatusField extends AppCompatActivity implements StatusItemClickListener{

    RecyclerView rv;
    status_adapter statusAdapter;
    List<String> username;
    List<Integer> image;

    RecyclerView post_recycle;
    List<String> nam;
    List<String> catego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_field);

        rv=findViewById(R.id.status_recycle);
        status();
       
        rv.setAdapter(statusAdapter);
    }

    private void status() {

        username=new ArrayList<>();
        image=new ArrayList<>();

        username.add("Tranquil");
        image.add(R.drawable.tranquil);
        username.add("Spade");
        image.add(R.drawable.spade);
        username.add("Jhankar");
        image.add(R.drawable.jhankar);
        username.add("Anonymous");
        image.add(R.drawable.anonymous);
        username.add("Yuva");
        image.add(R.drawable.yuvaa);
        username.add("Crazy4");
        image.add(R.drawable.craz);
        username.add("blogistan");
        image.add(R.drawable.blogistan);
        username.add("Sankalp");
        image.add(R.drawable.sankalp);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        statusAdapter = new status_adapter(this, username, image,this);

    }

    @Override
    public void onStatusClick(int position) {
        Intent i=new Intent(this,status_description.class);
        i.putExtra("StatusUser",username.get(position));
        i.putExtra("StatusLogo",image.get(position).toString());
        startActivity(i);

    }
}
