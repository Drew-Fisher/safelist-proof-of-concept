package com.example.drew.myapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

public class Contact implements Serializable {
    private int id;
    private String firstName,lastName,mobleNumber,homeNumber,email;
    private String AES = "AES";
    private ArrayList<byte[]> ivs;

    public Contact(String first){
        this.firstName = first;

    }

    public Contact(String first,String mobleNumber){
        this.firstName = first;
        this.mobleNumber = mobleNumber;
    }

    public Contact(String first,String mobleNumber,String lastName){
        this.firstName = first;
        this.mobleNumber = mobleNumber;
        this.lastName = lastName;
        ivs = new ArrayList<byte[]>();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void EncryptBio(String Listname)throws Exception{
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        this.ivs = new ArrayList<byte[]>();

        final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(Listname,null);
        final SecretKey secretKey = secretKeyEntry.getSecretKey();

        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);

        byte[] iv = cipher.getIV();
        this.ivs.add(iv);

        byte[] fnamebyte = cipher.doFinal(this.firstName.getBytes());
        String encryptedFirst = Base64.encodeToString(fnamebyte,Base64.DEFAULT);

        cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);

        byte[] iv2 = cipher.getIV();
        this.ivs.add(iv2);

        byte[] lnamebyte = cipher.doFinal(this.lastName.getBytes());
        String encryptedLast = Base64.encodeToString(lnamebyte,Base64.DEFAULT);

        cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);

        byte[] iv3 = cipher.getIV();
        this.ivs.add(iv3);

        byte[] phnambyte = cipher.doFinal(this.mobleNumber.getBytes());
        String encryptedMobile = Base64.encodeToString(phnambyte,Base64.DEFAULT);

        this.setFirstName(encryptedFirst);
        this.setLastName(encryptedLast);
        this.setMobleNumber(encryptedMobile);
    }

    public void DecryptBio(String Listname)throws Exception{
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(Listname,null);
        final SecretKey secretKey = secretKeyEntry.getSecretKey();

        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.ivs.get(0));
        IvParameterSpec ivParameterSpec2 = new IvParameterSpec(this.ivs.get(1));
        IvParameterSpec ivParameterSpec3 = new IvParameterSpec(this.ivs.get(2));

        final  Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);


        cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);


        byte[] enfirst = Base64.decode(this.getFirstName(),Base64.DEFAULT);
        byte[] decodfirst = cipher.doFinal(enfirst);

        cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec2);

        byte[] enlast = Base64.decode(this.getLastName(),Base64.DEFAULT);
        byte[] decodlast = cipher.doFinal(enlast);

        cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec3);

        byte[] enmbile = Base64.decode(this.getMobleNumber(),Base64.DEFAULT);
        byte[] decodmobile = cipher.doFinal(enmbile);

        String df = new String(decodfirst);
        this.setFirstName(df);

        String dl = new String(decodlast);
        this.setLastName(dl);

        String dp = new String(decodmobile);
        this.setMobleNumber(dp);
    }




    public void Encrypt(String passcode) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException {
        SecretKeySpec key = generateKey(passcode);
        Cipher c = Cipher.getInstance(AES);

        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] nambyte = c.doFinal(this.firstName.getBytes());
        String encryptFirst = Base64.encodeToString(nambyte, Base64.DEFAULT);

        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] lnambyte = c.doFinal(this.lastName.getBytes());
        String encryptLast = Base64.encodeToString(lnambyte, Base64.DEFAULT);

        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] passbyte = c.doFinal(this.mobleNumber.getBytes());
        String encryptPass = Base64.encodeToString(passbyte, Base64.DEFAULT);


        this.setFirstName(encryptFirst);
        this.setLastName(encryptLast);
        this.setMobleNumber(encryptPass);
    }

    private SecretKeySpec generateKey(String passcode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = passcode.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec sc = new SecretKeySpec(key, "AES");
        return sc;
    }

    public void Decrypt(String passcode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec key = generateKey(passcode);
        Cipher c = Cipher.getInstance(AES);

        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodfirst = Base64.decode(this.getFirstName(),Base64.DEFAULT);
        byte[] decodedf = c.doFinal(decodfirst);
        String df = new String(decodedf);
        this.setFirstName(df);

        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodlast = Base64.decode(this.getLastName(),Base64.DEFAULT);
        byte[] decodedl = c.doFinal(decodlast);
        String dl = new String(decodedl);
        this.setLastName(dl);

        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodmobil = Base64.decode(this.getMobleNumber(),Base64.DEFAULT);
        byte[] decodedm = c.doFinal(decodmobil);
        String dm = new String(decodedm);
        this.setMobleNumber(dm);
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void save(String pass,int i) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        this.Encrypt(pass);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobleNumber() {
        return mobleNumber;
    }

    public void setMobleNumber(String mobleNumber) {
        this.mobleNumber = mobleNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
