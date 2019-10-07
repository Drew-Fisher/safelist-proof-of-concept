package com.example.drew.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.view.LayoutInflater.from;
import static java.nio.file.Paths.get;

public class upload_list_adaptor extends RecyclerView.Adapter<upload_list_adaptor.MyViewHolder> {
    private MasterList m;
    private Context c;
    private String ID;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private int temp;


    public upload_list_adaptor(MasterList m2, Context cloud_upload,String ID2){
        m = m2;
        c = cloud_upload;
        ID = ID2;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout rel;

        public MyViewHolder(View v) {
            super(v);
            rel = itemView.findViewById(R.id.rel);
            textView = itemView.findViewById(R.id.upload_text_view);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = from(viewGroup.getContext()).inflate(R.layout.upload_list_layout,viewGroup, false);
        upload_list_adaptor.MyViewHolder vh = new upload_list_adaptor.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.textView.setText(m.getData().get(i).getListname());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = i;

                DocumentReference df = firestore.collection("user").document(ID);
                df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.getData() == null){
                                Map<String, Object> user = new HashMap<>();
                                Gson gson = new Gson();
                                String s = gson.toJson(m.getData().get(temp));
                                user.put(m.getData().get(temp).getListname(),s);
                                firestore.collection("user").document(ID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent i = new Intent(c,viewCloudList.class);
                                            i.putExtra("userID",ID);
                                            c.startActivity(i);
                                        }else {
                                            Toast t = Toast.makeText(c, "nope",Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                            }
                            else{
                                if(doc.getData().containsKey(m.getData().get(temp))){
                                    Toast t = Toast.makeText(c, "you already have a version of this list in you cloud please delete it before continuing",Toast.LENGTH_SHORT);
                                }else{
                                    Map<String,Object> cloud_data = doc.getData();
                                    Gson gson = new Gson();
                                    String s = gson.toJson(m.getData().get(temp));
                                    cloud_data.put(m.getData().get(temp).getListname(),s);
                                    firestore.collection("user").document(doc.getId()).update(cloud_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent i = new Intent(c,viewCloudList.class);
                                            i.putExtra("userID",ID);
                                            m.getData().remove(temp);
                                            try {
                                                m.save();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            c.startActivity(i);
                                        }
                                    });
                                }

                            }

                        }else{


                        }
                    }
                });
            }
        });
    }



    @Override
    public int getItemCount() {
        return m.getData().size();
    }


}
