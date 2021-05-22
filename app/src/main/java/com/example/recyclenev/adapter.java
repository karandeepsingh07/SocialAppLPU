package com.example.recyclenev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    Context context;
    List<String> mData;

    public adapter(Context context, List<String> mdata)
    {
        this.context=context;
        this.mData=mdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.resourcelayout,parent,false);
        return new adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nav.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nav = (TextView) itemView.findViewById(R.id.nav);
        }
    }
}
