package com.example.recyclenev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapterStatus extends RecyclerView.Adapter<PostAdapterStatus.PostViewHolder> {

    private ArrayList<StatusPC> items;
    private Context context;


    public PostAdapterStatus(ArrayList<StatusPC> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.status_profile,parent,false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapterStatus.PostViewHolder holder, int position) {
        final StatusPC statusPC=items.get(position);

        holder.username.setText(statusPC.getUsername());
        Glide.with(context).load(statusPC.getStatusUrl()).into(holder.imageViewStatus);
        holder.imageViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,status_description.class);
                intent.putExtra("username",statusPC.getUsername());
                //intent.putExtra("logo",statusPC.getUserImage());
                intent.putExtra("image",statusPC.getStatusUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewStatus;
        TextView username;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewStatus = itemView.findViewById(R.id.status_image);
            username= itemView.findViewById(R.id.status_username);
        }
    }
}
