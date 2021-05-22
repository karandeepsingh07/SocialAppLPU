package com.example.recyclenev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class checkAdpt extends RecyclerView.Adapter<checkAdpt.MyViewHolder> {
    Context context;
    List<String> s;

    public checkAdpt(Context context, List<String> s) {
        this.context = context;
        this.s = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.checkpost,parent,false);
        return new checkAdpt.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(s.get(position));
    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.checktv);
        }
    }
}
