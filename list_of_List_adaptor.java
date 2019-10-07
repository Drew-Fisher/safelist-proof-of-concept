package com.example.drew.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.view.LayoutInflater.from;
//import static com.example.drew.myapplication.Biometrics.isEnabled;
import static com.example.drew.myapplication.Biometrics.isHardware;
import static com.example.drew.myapplication.Biometrics.isPermission;
import static com.example.drew.myapplication.Biometrics.isfingerPrint;
import static com.example.drew.myapplication.Biometrics.issdkEnabled;

public class list_of_List_adaptor extends RecyclerView.Adapter<list_of_List_adaptor.MyViewHolder> {
    private ArrayList<contact_list> Data;
    private Context c;
    private AlertDialog.Builder builder;
    private EditText p1;
    private int q = 0;



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout rel;

        public MyViewHolder(View v) {
            super(v);
            rel = itemView.findViewById(R.id.rel);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public list_of_List_adaptor(MasterList m, Context listOfListActivity){
        Data = m.getData();
        this.c = listOfListActivity;
    }


    @Override
    public list_of_List_adaptor.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = from(viewGroup.getContext()).inflate(R.layout.lol_layout,viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull list_of_List_adaptor.MyViewHolder myViewHolder, final int i) {

        myViewHolder.textView.setText(Data.get(i).getListname());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                q = i;

                Intent z = new Intent(c,List_of_Contacts.class);
                z.putExtra("list",q);
                c.startActivity(z);

//                if(Data.get(q).getSecurity_level() > 0){
//                    if(Data.get(q).getSecurity_level() == 1){
//                        Intent z = new Intent(c,List_of_Contacts.class);
//                        z.putExtra("list",q);
//                        c.startActivity(z);
//                    }
//                    else if(Data.get(q).getSecurity_level() == 2){
//
//                    }
//                    else if(Data.get(i).getSecurity_level() == 3){
//
//                    }
//                    else if(Data.get(i).getSecurity_level() == 4){
//
//                    }
//                    else if(Data.get(i).getSecurity_level() == 5){
//
//                    }
//                }
//                else{
//                    Intent z = new Intent(c,List_of_Contacts.class);
//                    z.putExtra("list",i);
//                    c.startActivity(z);
//                }

            }
        });


    }
    int temp =0;
    public void unlocksec1(int i){
//        builder = new AlertDialog.Builder(c);
//        final LayoutInflater inf = LayoutInflater.from(c);
//        final View dialgo = inf.inflate(R.layout.enter_password,null);
//        final EditText p1 = dialgo.findViewById(R.id.editText2);
//        p1.setText(Integer.toString(i));
//        temp = i;
//
//        builder.setView(dialgo).setPositiveButton("sign in", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                String pass = p1.getText().toString();
//                try {
//                    if(Data.get(temp).Unlock(pass)){
//                        Toast t = Toast.makeText(c, "in",Toast.LENGTH_SHORT);
//                        t.show();
//                        Intent h = new Intent(c,List_of_Contacts.class);
//
//
//
//
//
//
//
//                        h.putExtra("passcode",pass);
//                        h.putExtra("list",temp);
//                        c.startActivity(h);
//                    }
//                    else{
//                        Toast t = Toast.makeText(c, "out",Toast.LENGTH_SHORT);
//                        t.show();
//                    }
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog a = builder.create();
//        a.show();
    }
    private static final String KEY_Name = "test";

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void unlocksec4(int i) throws Exception {
        Signature signature;
        signature = initSignature(Data.get(i).getListname());
        temp = i;

        BiometricPrompt b = new BiometricPrompt.Builder(this.c)
                .setTitle("test")
                .setNegativeButton("t", c.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //biometricCallback.onAuthenticationCancelled();
                                        }
                }).build();
        BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationonCallback();
        CancellationSignal cancellationSignal = new CancellationSignal();
        if(signature != null){
            b.authenticate(new BiometricPrompt.CryptoObject(signature),cancellationSignal,c.getMainExecutor(),authenticationCallback);

        }


    }

//    private CancellationSignal getCancellationSignal(){
//        CancellationSignal cancellationSignal = new CancellationSignal();
//        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
//            @Override
//            public void onCancel() {
//
//            }
//        });
//    }
    @Override
    public int getItemCount() {
        return Data.size();
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
                Intent i = new Intent(c,List_of_Contacts.class);
                i.putExtra("list",temp);
                c.startActivity(i);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }

    private boolean isSupportBiometricPrompt(){
        PackageManager packageManager = c.getPackageManager();
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