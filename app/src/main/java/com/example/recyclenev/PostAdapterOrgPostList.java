package com.example.recyclenev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapterOrgPostList extends RecyclerView.Adapter<PostAdapterOrgPostList.PostViewHolder> {

    private ArrayList<PostDetailPC> items;
    private Context context;


    public PostAdapterOrgPostList(ArrayList<PostDetailPC> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.organisation_post_list_item,parent,false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapterOrgPostList.PostViewHolder holder, int position) {
        final PostDetailPC postDetailPC=items.get(position);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String uid=user.getUid();

        Glide.with(context).load(postDetailPC.getEventImageUrl()).into(holder.imageViewOrgPost);

        holder.imageViewOrgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PostDescriptionActivity.class);
                intent.putExtra("pid",postDetailPC.getpUid());
                intent.putExtra("upid",postDetailPC.getPpUid());
                intent.putExtra("epid",uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewOrgPost;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrgPost=itemView.findViewById(R.id.imageViewPostItem);
        }
    }
}
