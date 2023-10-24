package com.ass2.i200581;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    Context context;
    List<itemModel> mylist;

    public myAdapter(Context context, List<itemModel> mylist) {
        this.context = context;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public myAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.itemlist,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.MyViewHolder holder, int position) {
        holder.name.setText(mylist.get(position).getName());
        holder.price.setText(mylist.get(position).getPrice());
        holder.location.setText(mylist.get(position).getLocation());
        holder.date.setText(mylist.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,location,price,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

        }
    }
}
