package com.example.recyclenev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapterAnnouncemnet extends RecyclerView.Adapter<PostAdapterAnnouncemnet.PostViewHolder>{
    private ArrayList<AnnouncementPC> items;
    private Context context;

    public PostAdapterAnnouncemnet(ArrayList<AnnouncementPC> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public PostAdapterAnnouncemnet.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.anouncement_item,parent,false);

        return new PostAdapterAnnouncemnet.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterAnnouncemnet.PostViewHolder holder, int position) {
        AnnouncementPC announcementPC=items.get(position);

        holder.textViewUsername.setText(announcementPC.getUserName());
        holder.textViewTitle.setText(announcementPC.getDescription());
        holder.textViewDescription.setText(announcementPC.getTitle());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        TextView textViewUsername,textViewTitle,textViewDescription;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername=itemView.findViewById(R.id.textViewAnnUsername);
            textViewTitle=itemView.findViewById(R.id.textViewAnnTitle);
            textViewDescription=itemView.findViewById(R.id.textViewAnnDescription);
        }
    }
}
