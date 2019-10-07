package com.example.drew.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.os.Build.ID;
import static android.view.LayoutInflater.from;

public class list_cloud_list_adaptor extends RecyclerView.Adapter<list_cloud_list_adaptor.MyViewHolder> {
    private MasterList m;
    private ArrayList<contact_list> list_from_cloud;
    private Context cur;
    private AlertDialog.Builder Builder,Builder2;
    private int temp;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String ID;


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout rel;

        public MyViewHolder(View v) {
            super(v);
            rel = itemView.findViewById(R.id.rel);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public list_cloud_list_adaptor(ArrayList<contact_list> lists, Context c,MasterList me,String ID2){
        this.list_from_cloud = lists;
        this.cur = c;
        this.m = me;
        this.ID = ID2;
    }


    @NonNull
    @Override
    public list_cloud_list_adaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = from(viewGroup.getContext()).inflate(R.layout.lol_layout,viewGroup, false);
        list_cloud_list_adaptor.MyViewHolder vh = new list_cloud_list_adaptor.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull list_cloud_list_adaptor.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(list_from_cloud.get(i).getListname());
        temp = i;
        Builder = new AlertDialog.Builder(cur);
        final LayoutInflater inf = LayoutInflater.from(cur);
        final View dialgo = inf.inflate(R.layout.download_list_option,null);
        final CheckBox check = dialgo.findViewById(R.id.checkBox);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Builder.setView(dialgo).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(check.isChecked()){
                            DocumentReference df = firestore.collection("user").document(ID);
                            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        Map<String, Object> cloud_data = doc.getData();
                                        cloud_data.remove(list_from_cloud.get(temp).getListname());
                                        firestore.collection("user").document(ID).set(cloud_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    m.add(list_from_cloud.get(temp));
                                                    try {
                                                        m.save();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Intent i = new Intent(cur,ListOfListActivity.class);
                                                    cur.startActivity(i);
                                                }
                                                else{
                                                    Toast.makeText(cur,"Unable to download and delete list",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                }
                            });
                        }else{
                            m.add(list_from_cloud.get(temp));
                            try {
                                m.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent z = new Intent(cur,ListOfListActivity.class);
                            cur.startActivity(z);
                        }



//                        Builder2 = new AlertDialog.Builder(cur);
//                        LayoutInflater inf2 = from(cur);
//                        View diag = inf.inflate(R.layout.delete_cloud_list,null);
//                        Builder2.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                DocumentReference df = firestore.collection("user").document(ID);
//                                df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if(task.isSuccessful()){
//                                            DocumentSnapshot doc = task.getResult();
//                                            Map<String, Object> cloud_data = doc.getData();
//                                            cloud_data.remove(list_from_cloud.get(temp).getListname());
//                                            firestore.collection("user").document(ID).set(cloud_data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful()){
//                                                        m.add(list_from_cloud.get(temp));
//                                                        Intent i = new Intent(cur,ListOfListActivity.class);
//                                                        cur.startActivity(i);
//                                                    }
//                                                    else{
//
//                                                    }
//                                                }
//                                            });
//
//
//                                        }
//                                    }
//                                });
//                            }
//                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                m.add(list_from_cloud.get(temp));
//                                try {
//                                    m.save();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                Intent z = new Intent(cur,ListOfListActivity.class);
//                                cur.startActivity(z);
//                            }
//                        });
//                        AlertDialog b = Builder2.create();
//                        b.show();

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });AlertDialog a = Builder.create();
                a.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_from_cloud.size();
    }
}
