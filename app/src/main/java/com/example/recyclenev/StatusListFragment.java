package com.example.recyclenev;


import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class StatusListFragment extends Fragment{
    List<StatusPC> statusList;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    PostAdapterStatus postAdapter;
    ProgressBar progressBar;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_status_list, container, false);
        database= FirebaseDatabase.getInstance();
        reference=database.getReference().child("Status");
        recyclerView = v.findViewById(R.id.status_recycle);
        progressBar = v.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        statusList=new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapterStatus((ArrayList<StatusPC>) statusList,getContext());
        recyclerView.setAdapter(postAdapter);

        status();
        return v;
    }

    private void status() {

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot orgSnapshot : dataSnapshot.getChildren()) {
                    StatusPC statusPC = orgSnapshot.getValue(StatusPC.class);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                    String currentDateTimeString = simpleDateFormat.format(new Date());
                    Date date2 = null;
                    try {
                        date2 = simpleDateFormat.parse(currentDateTimeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long date1 = Long.parseLong(statusPC.getUploadTime());
                    long different =  date2.getTime()-date1;

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = different / daysInMilli;
                    Toast.makeText(getActivity(), ""+elapsedDays, Toast.LENGTH_SHORT).show();
                    if(elapsedDays<1) {
                        statusList.add(statusPC);
                        progressBar.setVisibility(View.GONE);
                        postAdapter.notifyDataSetChanged();
                    }
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
}
