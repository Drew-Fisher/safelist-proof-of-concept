package com.example.drew.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class list_of_contacts_adapator extends RecyclerView.Adapter<list_of_contacts_adapator.MyViewHolder>{
    private ArrayList<Contact> Data;
    private int listnum;
    private Context nh;


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout rel;
        public MyViewHolder(View v) {
            super(v);
            rel = itemView.findViewById(R.id.rel);
            textView = itemView.findViewById(R.id.contactlistView);
        }
    }

    public list_of_contacts_adapator(contact_list c, Context f,int d){
        Data = c.getList();
        listnum = d;
        nh = f;
    }


    @Override
    public list_of_contacts_adapator.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loc_layout,viewGroup, false);
        list_of_contacts_adapator.MyViewHolder vh = new list_of_contacts_adapator.MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull list_of_contacts_adapator.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(Data.get(i).getFirstName()+" "+Data.get(i).getLastName());
        final int temp = i;
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(nh,viewContact.class);
                z.putExtra("list",listnum);
                z.putExtra("contact",temp);
                nh.startActivity(z);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
