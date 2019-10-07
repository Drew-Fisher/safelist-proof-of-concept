package com.example.drew.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button test,make,test2,save,load,demo;
    private TextView t1;
    private JSONObject passed;
    private MasterList m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        m = (MasterList)getApplication();
//
//        Intent q = getIntent();
//        if(q.getStringExtra("save") != null){
//            if(q.getStringExtra("save").equals("y")){
//
//                Toast t = Toast.makeText(getBaseContext(), Integer.toString(m.getData().size()),Toast.LENGTH_SHORT);
//                t.show();
//
//
//                try {
//                    save();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        test = findViewById(R.id.button);
//        t1 = findViewById(R.id.textView2);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Contact cfd = new Contact("df","1808888888");
//                try {
//                    m.getData().get(0).add(cfd,"f");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        demo = findViewById(R.id.Demo);
//        demo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
//                //MasterList tampm =(MasterList)getApplication();
//                //tampm.setData(m.getData());
//                startActivity(i);
//            }
//        });
//
//        save = findViewById(R.id.button6);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    save();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        make = findViewById(R.id.button2);
//        make.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                travel();
//            }
//        });
//
//        test2 = findViewById(R.id.button5);
//
//        test2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Contact cfd = new Contact("df","1808888888");
//                try {
//                    m.getData().get(0).add(cfd,"f");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        load = findViewById(R.id.button7);
//        load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    m.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                t1.setText(m.check());
//            }
//        });

        Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
        startActivity(i);
    }
    public void travel(){
        Intent i = new Intent(this,make_contact.class);
        startActivity(i);
    }

    public void gettest() throws JSONException {
        Intent i = getIntent();
        passed = new JSONObject();
        passed.put("First Name",i.getExtras().getString("First Name"));
        passed.put("Last Name",i.getExtras().getString("Last Name"));
        passed.put("Phone Number",i.getExtras().getString("Phone Number"));
    }

    public void load() throws IOException, ClassNotFoundException {
        m.load();
    }

    public void save() throws IOException {
        m.save();
    }

}
