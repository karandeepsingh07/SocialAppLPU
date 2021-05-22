package com.example.recyclenev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class status_adapter extends RecyclerView.Adapter<status_adapter.ViewHolder> {
    Context context;
    List<String> username;
    List<Integer> image;
    private StatusItemClickListener clickListener;
    public status_adapter(Context context, List<String> username, List<Integer> image,StatusItemClickListener clickListener) {
        this.context = context;
        this.username = username;
        this.image = image;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.status_profile,parent,false);
        return new ViewHolder(v,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(username.get(position));
        Glide.with(context).load(image.get(position)).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView circleImageView;
        TextView username;
        StatusItemClickListener itemClickListener;
        public ViewHolder( View itemView, StatusItemClickListener clickListener) {
            super(itemView);
            circleImageView= itemView.findViewById(R.id.status_image);
            username= itemView.findViewById(R.id.status_username);
            this.itemClickListener=clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onStatusClick(getAdapterPosition());
        }
    }
}
