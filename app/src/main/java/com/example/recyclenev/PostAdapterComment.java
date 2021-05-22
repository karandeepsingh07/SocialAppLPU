package com.example.recyclenev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapterComment extends RecyclerView.Adapter<PostAdapterComment.PostViewHolder>{
    private ArrayList<CommentPC> items;
    private Context context;
    private int lastPosition = -1;

    public PostAdapterComment(ArrayList<CommentPC> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public PostAdapterComment.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.comment_layout,parent,false);

        return new PostAdapterComment.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterComment.PostViewHolder holder, int position) {
        CommentPC commentPC=items.get(position);

        holder.textViewUsername.setText(commentPC.getUsername());
        holder.textViewComment.setText(commentPC.getComment());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        TextView textViewUsername,textViewComment;
        CircleImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername=itemView.findViewById(R.id.textViewUsernameComment);
            textViewComment=itemView.findViewById(R.id.textViewComment);
            imageView=itemView.findViewById(R.id.userDP);
        }
    }
}
