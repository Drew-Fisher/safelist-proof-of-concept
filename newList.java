package com.example.drew.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Collections;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class newList extends AppCompatActivity {

    private EditText name,sec;
    private Button back,save;
    private int selectedLevel;
    private Spinner s;
    private EditText enteredPasword,confirmPassword;
    private Context cx = this;
    private Cipher cipher;
    private KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        name = findViewById(R.id.new_list_name);
        save = findViewById(R.id.save_new_List);
        selectedLevel = 0;
        enteredPasword = findViewById(R.id.newlistPassword);
        confirmPassword = findViewById(R.id.confirmnewlistPassword);
        enteredPasword.setVisibility(View.INVISIBLE);
        confirmPassword.setVisibility(View.INVISIBLE);


        back = findViewById(R.id.cancle_new_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
                startActivity(i);
            }
        });


        s = (Spinner) findViewById(R.id.spinner);
        String[] secLevels = new String[]{"No Password","Password","Biometric","Deadlock","Biometric Deadlock"};
        ArrayAdapter<String> secl = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line
                , secLevels);
        s.setAdapter(secl);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(s.getSelectedItem().toString().equals("No Password")){
                    selectedLevel = 0;
                    enteredPasword.setVisibility(View.INVISIBLE);
                    confirmPassword.setVisibility(View.INVISIBLE);
                }
                else if(s.getSelectedItem().toString().equals("Password")){
                    selectedLevel = 1;
                    enteredPasword.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);

                }
                else if(s.getSelectedItem().toString().equals("Biometric")){
                    selectedLevel = 2;
                    enteredPasword.setVisibility(View.INVISIBLE);
                    confirmPassword.setVisibility(View.INVISIBLE);
                }
                else if(s.getSelectedItem().toString().equals("Deadlock")){
                    selectedLevel = 3;
                    enteredPasword.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);
                }
                else if(s.getSelectedItem().toString().equals("Biometric Deadlock")){
                    selectedLevel = 4;
                    enteredPasword.setVisibility(View.INVISIBLE);
                    confirmPassword.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast t =Toast.makeText(getBaseContext(), "Something went wrong",Toast.LENGTH_SHORT);
                    t.show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                if(selectedLevel == 0){
                    try {
                        contact_list c = new contact_list(name.getText().toString(),selectedLevel,"aaa");
                        MasterList m = (MasterList) getApplication();
                        m.add(c);
                        try {
                            m.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
                        i.putExtra("save","y");
                        startActivity(i);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }

                }
                else if(selectedLevel == 1||selectedLevel ==3){
                    try {
                        savelv1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(selectedLevel == 2|| selectedLevel ==4){
                    try {
                        savelv2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        back = findViewById(R.id.cancle_new_list);


    }

    public void savelv1() throws IOException {
        if(enteredPasword.getText().toString().equals(confirmPassword.getText().toString())){
            contact_list c = null;
            try {
                c = new contact_list(name.getText().toString(),selectedLevel,enteredPasword.getText().toString());
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            MasterList m = (MasterList) getApplication();
            m.add(c);
            m.save();
            Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
            i.putExtra("save","y");
            startActivity(i);
        }
        else{
            Toast t =Toast.makeText(getBaseContext(), "Your passwords do not match",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void savelv2() throws Exception{
        //KeyPair keyPair = generateKeyPair(name.getText().toString(),true);
        SecretKey keyPair = generateKeyAes(name.getText().toString());
        //Signature signature = initSignature(name.getText().toString());
        //Signature signature = Signature.getInstance();
        BiometricPrompt mBiometricPrompt = new BiometricPrompt.Builder(this).setTitle("lock").setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).build();
        BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationonCallback();
        CancellationSignal cancellationSignal = new CancellationSignal();
        mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(cipher),cancellationSignal,getMainExecutor(),authenticationCallback);
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

                contact_list c = new contact_list(name.getText().toString(),selectedLevel);
                MasterList m = (MasterList) getApplication();
                m.add(c);
                try {
                    m.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(cx,ListOfListActivity.class);
                startActivity(i);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
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



    @RequiresApi(api = Build.VERSION_CODES.P)
    private SecretKey generateKeyAes(String Listname)throws Exception{
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        keyGenerator.init(new KeyGenParameterSpec.Builder(Listname,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setUserAuthenticationRequired(false)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .build());
        return keyGenerator.generateKey();
    }

    private boolean initCipher(String ListName)throws Exception{
        cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(ListName,null);
            return true;

    }




















    private Signature initSignature(String keyname) throws Exception {
        KeyPair keypair = getKeyPair(keyname);
        if(keypair != null){
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keypair.getPrivate());
            return signature;
        }
        return null;
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    private KeyPair generateKeyPair(String keyName,boolean invalidatedByBiometricEnrollment) throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                // Require the user to authenticate with a biometric to authorize every use of the key
                .setUserAuthenticationRequired(true)
                // Generated keys will be invalidated if the biometric templates are added more to user device
                .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);

        keyPairGenerator.initialize(builder.build());

        return keyPairGenerator.generateKeyPair();
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private KeyPair rsaPair(String keyname,Boolean invalidatedByBiometricEnrollment)throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","AndroidKeyStore");
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(keyname,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256,KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build();
        keyPairGenerator.initialize(keyGenParameterSpec);
        return keyPairGenerator.generateKeyPair();
    }
}
