package com.example.drew.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class viewContact extends AppCompatActivity {
    private Button Back, Edit;
    private TextView contact_name, Mobile_Number, Email;
    private MasterList m;
    private int c, z;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        m = (MasterList) getApplication();
        Back = findViewById(R.id.view_contact_back);
        contact_name = findViewById(R.id.Contact_name);
        Mobile_Number = findViewById(R.id.view_Mobile);
        Edit = findViewById(R.id.Edit_contact);
        Intent i = getIntent();
        c = i.getIntExtra("list", 0);
        z = i.getIntExtra("contact", 0);
        getcontactName();
        getMobile();

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(viewContact.this);
                final LayoutInflater inf = LayoutInflater.from(getBaseContext());
                final View dialgo = inf.inflate(R.layout.deletecon_pop,null);
                final int temp = c;
                final contact_list Data = m.getData().get(temp);

                builder.setView(dialgo).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            m.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        m.getData().get(c).getList().remove(z);
                        try {
                            m.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent z = new Intent(getBaseContext(),List_of_Contacts.class);
                        z.putExtra("list",c);
                        startActivity(z);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog a = builder.create();
                a.show();
            }
        });

        Mobile_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent z = new Intent(Intent.ACTION_CALL);
                    String call = Mobile_Number.getText().toString();
                    z.setData(Uri.parse("tel:" + call));
                    //Toast.makeText(getBaseContext(),call,Toast.LENGTH_SHORT).show();
                    z.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(z);
                }catch (SecurityException e) {
                    Toast.makeText(getBaseContext(),"fail",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(getBaseContext(),List_of_Contacts.class);
                z.putExtra("list",c);
                z.putExtra("viewed",1);
                startActivity(z);
            }
        });
    }
    private void getcontactName(){
        if(m.getData().get(c).getList().get(z).getLastName() != null){
            contact_name.setText(m.getData().get(c).getList().get(z).getFirstName() +" "+m.getData().get(c).getList().get(z).getLastName());
        }
        else{
            contact_name.setText(m.getData().get(c).getList().get(z).getFirstName());
        }
    }
    private void getMobile(){
        Mobile_Number.setText(m.getData().get(c).getList().get(z).getMobleNumber());
    }

}
