package com.example.drew.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class viewCloudList extends AppCompatActivity {
    private String user;
    private Button Upload,Download,Back;
    private FirebaseFirestore firestore;
    private RecyclerView v;
    private RecyclerView.LayoutManager l;
    private RecyclerView.Adapter ad;
    private ArrayList<contact_list> listmap;
    private MasterList m;
    private Context c;
    private FirebaseAuth mAuth;
    private Map<String,Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cloud_list);
        m = (MasterList) getApplication();
        c = this;
        Intent I = getIntent();
        user = I.getStringExtra("userID");
        firestore = FirebaseFirestore.getInstance();
        DocumentReference df = firestore.collection("user").document(user);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getData() == null){
                        userData = new HashMap<String,Object>();
                    }else{
                        userData = task.getResult().getData();
                    }

                        Set<String> keys = userData.keySet();
                        listmap = new ArrayList<contact_list>();
                        //String[] s = (String[]) keys.toArray();
                        Gson gson = new Gson();
                        for(String key: keys){

                            contact_list tp = gson.fromJson(userData.get(key).toString(),contact_list.class);
                            listmap.add(tp);

                        }
                        v = (RecyclerView) findViewById(R.id.list_of_cloud_list);
                        l = new LinearLayoutManager(c);
                        v.setLayoutManager(l);

                        ad = new list_cloud_list_adaptor(listmap,c,m,user);
                        v.setAdapter(ad);


                }else{
                    Toast.makeText(getBaseContext(),"no connection to server",Toast.LENGTH_SHORT).show();
                }
            }
        });




        Button true_back = findViewById(R.id.button8);
        true_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
                startActivity(i);
            }
        });



        Upload = findViewById(R.id.Upload_List_Screen);
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(getBaseContext(), cloud_upload.class);
                z.putExtra("userID",user);
                startActivity(z);
            }
        });

        Back = findViewById(R.id.back_from_cloud_list);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), cloudLogin.class);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(i);
            }
        });
    }
}
