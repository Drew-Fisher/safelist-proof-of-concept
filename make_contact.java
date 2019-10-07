package com.example.drew.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class make_contact extends AppCompatActivity implements Serializable {
    private EditText first,last,num;
    private Button save,back;
    private MasterList m;
    private int c;
    private String pass;
    private AlertDialog.Builder builder;
    private boolean t = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contact);
        m = (MasterList)getApplication();
        first = findViewById(R.id.editText);
        last = findViewById(R.id.lastName);
        num = findViewById(R.id.number);
        back = findViewById(R.id.button3);
        save = findViewById(R.id.button4);
        Intent z = getIntent();
        z.getExtras();
        c = z.getIntExtra("list",0);


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear(first);
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear(last);
            }
        });

        num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear(num);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rev();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {


                if(m.getData().get(c).getSecurity_level() == 0){
                    try {
                        pass = "aaa";
                        //m.getData().get(c).decryptall("aaa");
                        send();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(m.getData().get(c).getSecurity_level()==1||m.getData().get(c).getSecurity_level()==3){
                    getpassCode();

                }
                else if(m.getData().get(c).getSecurity_level()==2 || m.getData().get(c).getSecurity_level() == 4){
                    pass = m.getData().get(c).getListname();
                        //m.getData().get(c).decryptallbio(pass);
                    try {
                        send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getpassCode(){
        builder = new AlertDialog.Builder(make_contact.this);
        final LayoutInflater inf = LayoutInflater.from(this);
        final View dialgo = inf.inflate(R.layout.enter_password,null);
        final EditText p1 = dialgo.findViewById(R.id.editText2);
        final int temp = c;
        final contact_list Data = m.getData().get(temp);


        builder.setView(dialgo).setPositiveButton("sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                pass = p1.getText().toString();
                try {
                    if(Data.Unlock(pass)){
                        //Data.decryptall(pass);
                        send();
                    }else{
                        Toast.makeText(getBaseContext(),"THe password you entered was incorrect",Toast.LENGTH_SHORT).show();
                    }

                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog agf = builder.create();
        agf.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void send() throws Exception {
        String F = first.getText().toString();
        String L = last.getText().toString();
        String P = num.getText().toString();
        boolean sd = true;
        if(first.getText().toString() == "" || first.getText().toString() == "First name"){
            sd = false;
        }
        if(last.getText().toString() == "" || last.getText().toString() == "Last name"){
            L = "";
        }
        if(num.getText().toString() == "" || num.getText().toString() == "Phone Number"){
            sd = true;
        }

         if(sd == true){
             Intent i = new Intent(getApplicationContext(), List_of_Contacts.class);
             Contact newc = new Contact(F,P,L);
             m.getData().get(c).add(newc,pass);
             i.putExtra("list",c);
             //i.putExtra("viewed",1);
             m.save();
             try {
                 m.load();
             } catch (ClassNotFoundException e) {
                 e.printStackTrace();
             }
             startActivity(i);
         }
    }

    private void rev() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Intent i = new Intent(this,List_of_Contacts.class);
        i.putExtra("list",c);
        startActivity(i);
    }

    private void clear(EditText t){
        t.setText("");
    }
}
