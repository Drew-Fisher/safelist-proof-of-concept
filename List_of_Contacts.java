package com.example.drew.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class List_of_Contacts extends AppCompatActivity {
    private int c;
    private MasterList m;
    private Button newContact,encode,decode,Delete;
    private RecyclerView v;
    private RecyclerView.LayoutManager l;
    private RecyclerView.Adapter ad;
    private String pass;
    private AlertDialog.Builder builder;
    private int countDown,viewed;
    private TextView dislName;


    private Context mContext = this;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of__contacts);
        countDown = 4;
        Intent i = getIntent();
        i.getExtras();
        c = i.getIntExtra("list",0);
        m = (MasterList)getApplication();
        newContact = findViewById(R.id.newContact);
        dislName = findViewById(R.id.textView5);
        dislName.setText(m.getData().get(c).getListname());
        viewed = i.getIntExtra("viewed",0);

        if(viewed == 1){
            if(m.getData().get(c).getSecurity_level() == 2 ||m.getData().get(c).getSecurity_level() == 4){
                pass = m.getData().get(c).getListname();
                runl();
            }else if(m.getData().get(c).getSecurity_level() == 1||m.getData().get(c).getSecurity_level() == 3){
                Toast t = Toast.makeText(mContext, "The password you entered was incorrect",Toast.LENGTH_SHORT);
                t.show();
                passloop2();
            }else{
                pass = "aaa";
                runl();
            }
        }
        else if(m.getData().get(c).getSecurity_level() == 0){
            contact_list Data = m.getData().get(c);
            pass = "aaa";
            try {
                Data.decryptall("aaa");
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
            runl();
        }
        else if(m.getData().get(c).getSecurity_level() == 1){
            passloop();
        }
        else if(m.getData().get(c).getSecurity_level() == 2 || m.getData().get(c).getSecurity_level() ==4){
            try {
                final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

            BiometricPrompt b = new BiometricPrompt.Builder(this)
                    .setTitle("Verify your fingerprint to unlock the list")
                    .setNegativeButton("cancel", this.getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent z = new Intent(getBaseContext(),ListOfListActivity.class);
                            startActivity(z);
                        }
                    }).build();
            BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationonCallback();
            CancellationSignal cancellationSignal = new CancellationSignal();
            b.authenticate(new CancellationSignal(),getMainExecutor(),authenticationCallback);

        }
        else if(m.getData().get(c).getSecurity_level() == 3){
            deadpass();
        }
//        else if(m.getData().get(c).getSecurity_level() == 4){
//            try {
//                final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            }
//
//            BiometricPrompt b = new BiometricPrompt.Builder(this)
//                    .setTitle("test")
//                    .setNegativeButton("t", this.getMainExecutor(), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //biometricCallback.onAuthenticationCancelled();
//                        }
//                    }).build();
//            BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationonCallback();
//            CancellationSignal cancellationSignal = new CancellationSignal();
//            b.authenticate(new CancellationSignal(),getMainExecutor(),authenticationCallback);
//        }

        if((String) i.getStringExtra("passcode") == null){

        }
        else{
            pass = (String) i.getStringExtra("passcode");
            try {
                m.getData().get(c).decryptall(pass);
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

        Delete = findViewById(R.id.button9);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(List_of_Contacts.this);
                final LayoutInflater inf = LayoutInflater.from(getBaseContext());
                final View dialgo = inf.inflate(R.layout.deletelistprompt,null);
                final contact_list Data = m.getData().get(c);

                builder.setView(dialgo).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m.getData().remove(c);
                        try {
                            m.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent I = new Intent(getBaseContext(),ListOfListActivity.class);
                        startActivity(I);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog a = builder.create();
                a.show();
            }
        });

        Button back = findViewById(R.id.button13);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(),ListOfListActivity.class);

                startActivity(i);
            }
        });




        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m.getData().get(c).getSecurity_level() == 2||m.getData().get(c).getSecurity_level() ==4){
                    Intent z = new Intent(mContext,make_contact.class);
                    z.putExtra("list",c);
//                    try {
//                        m.getData().get(c).encryptallbio(m.getData().get(c).getListname());
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    startActivity(z);
                }
                else{
                    Intent z = new Intent(mContext,make_contact.class);
                    z.putExtra("list",c);
//                    try {
//                        m.getData().get(c).encryptall(pass);
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    }
                    startActivity(z);
                }


            }
        });



    }

    private void passloop(){
        builder = new AlertDialog.Builder(List_of_Contacts.this);
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
                    if(m.getData().get(c).Unlock(pass)){
                        m.getData().get(c).decryptall(pass);
                    }
                    else{

                        Toast t = Toast.makeText(mContext, "The password you entered was incorrect",Toast.LENGTH_SHORT);
                        t.show();
                        passloop();
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
                }
                if(m.getData().get(c).Unlock(pass)){
                    runl();
                }

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog a = builder.create();
        a.show();
    }

    private void passloop2(){
        builder = new AlertDialog.Builder(List_of_Contacts.this);
        final LayoutInflater inf = LayoutInflater.from(this);
        final View dialgo = inf.inflate(R.layout.enter_password,null);
        final EditText p1 = dialgo.findViewById(R.id.editText2);
        final int temp = c;
        final contact_list Data = m.getData().get(temp);

        builder.setView(dialgo).setPositiveButton("sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                pass = p1.getText().toString();
                    if(m.getData().get(c).Unlock(pass)){
                        runl();
                    }
                    else{

                        Toast t = Toast.makeText(mContext, "The password you entered was incorrect",Toast.LENGTH_SHORT);
                        t.show();
                        passloop2();
                    }

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog a = builder.create();
        a.show();
    }

    private void deadpass(){
        if(countDown ==0){
            m.getData().remove(c);
            try {
                m.save();
                Intent I = new Intent(getBaseContext(),ListOfListActivity.class);
                startActivity(I);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            builder = new AlertDialog.Builder(List_of_Contacts.this);
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
                            Data.decryptall(pass);

                        }
                        else{

                                Toast t = Toast.makeText(mContext, "The password you entered is not Correct you have "+ (countDown) +" left before the Data is Deleted",Toast.LENGTH_SHORT);
                                t.show();
                                countDown--;
                                dialogInterface.cancel();

                                deadpass();



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
                    }
                    if(Data.Unlock(pass)){
                        runl();
                    }
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog a = builder.create();
            a.show();
        }

    }

    private void runl(){
        v = (RecyclerView) findViewById(R.id.listofcontacts);
        l = new LinearLayoutManager(this);
        v.setLayoutManager(l);

        ad = new list_of_contacts_adapator(m.getData().get(c),this,c);
        v.setAdapter(ad);
        final Context mContext = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationonCallback(){
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if(viewed != 3){
                    try {
                        pass = m.getData().get(c).getListname();
                        m.getData().get(c).decryptallbio(pass);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runl();
                }else{

                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if(m.getData().get(c).getSecurity_level() == 4){
                    if(countDown ==0){
                        m.getData().remove(c);
                        try {
                            m.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
                        startActivity(i);
                    }
                    else{
                            countDown--;
                        Toast v = Toast.makeText(mContext, "Authentication Failed "+ (countDown+1) +" left before the Data is Deleted",Toast.LENGTH_SHORT);
                            v.show();

                    }
                }
            }
        };
    }

    private boolean isSupportBiometricPrompt(){
        PackageManager packageManager = this.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            return true;
        }
        return false;
    }

    private KeyPair getKeyPair(String keyName) throws Exception {
        KeyStore keystore = KeyStore.getInstance("AndroidKeyStore");
        keystore.load(null);
        if(keystore.containsAlias(keyName)){
            PublicKey publicKey = keystore.getCertificate(keyName).getPublicKey();
            PrivateKey privateKey = (PrivateKey) keystore.getKey(keyName,null);
            return new KeyPair(publicKey,privateKey);
        }
        return null;
    }

    private Signature initSignature(String keyname) throws Exception {
        KeyPair keypair = getKeyPair(keyname);
        if(keypair != null){
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(keypair.getPrivate());
            return signature;
        }
        return null;
    }
}
