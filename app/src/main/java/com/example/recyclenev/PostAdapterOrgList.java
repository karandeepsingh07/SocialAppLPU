package com.example.recyclenev;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapterOrgList extends RecyclerView.Adapter<PostAdapterOrgList.PostViewHolder> {

    private ArrayList<OrganisationPC> items;
    private Context context;

    public PostAdapterOrgList(ArrayList<OrganisationPC> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.organisation_list_item,parent,false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        OrganisationPC organisationPC=items.get(position);

        final String uid=organisationPC.getUid();
        holder.textViewOrgTitle.setText(organisationPC.getTitle());
        Glide.with(context).load(organisationPC.getImageUrl()).into(holder.imageViewOrg);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,OrganisationProfileActivity.class);
                intent.putExtra("Udi",uid);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
            return items.size();
            }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        TextView textViewOrgTitle;
        LinearLayout linearLayout;
        ImageView imageViewOrg;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrgTitle=itemView.findViewById(R.id.textViewOrgTitle);
            imageViewOrg=itemView.findViewById(R.id.imageViewOrganisation);
            linearLayout=itemView.findViewById(R.id.orgListLayout);
        }
    }
}
