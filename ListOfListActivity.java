package com.example.drew.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ListOfListActivity extends AppCompatActivity {
private RecyclerView v;
private RecyclerView.LayoutManager l;
private RecyclerView.Adapter ad;
private Button cloud,newlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_list);

        v = (RecyclerView) findViewById(R.id.list_of_list);
        l = new LinearLayoutManager(this);
        v.setLayoutManager(l);

        MasterList m = (MasterList) getApplication();
        try {
            m.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cloud = findViewById(R.id.cloud_from_list);
        //cloud.setText(Integer.toString(m.getData().size()));
        newlist = findViewById(R.id.newlist);
        newlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),newList.class);
                startActivity(i);
            }
        });

        cloud = findViewById(R.id.cloud_from_list);
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),cloudLogin.class);
                startActivity(i);
            }
        });


        ad = new list_of_List_adaptor(m,this);
        v.setAdapter(ad);


    }
}
