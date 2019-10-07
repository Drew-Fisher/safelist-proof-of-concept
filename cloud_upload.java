package com.example.drew.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class cloud_upload extends AppCompatActivity {
    private MasterList m;
    private String ID;
    private RecyclerView v;
    private RecyclerView.LayoutManager l;
    private RecyclerView.Adapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_upload);
        Intent I = getIntent();
        ID = I.getStringExtra("userID");

        m = (MasterList) getApplication();

        v = (RecyclerView) findViewById(R.id.cloud_upload_list);
        l = new LinearLayoutManager(this);
        v.setLayoutManager(l);

        ad = new upload_list_adaptor(m,this,ID);
        v.setAdapter(ad);


    }
}
