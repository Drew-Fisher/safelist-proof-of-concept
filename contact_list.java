package com.example.drew.myapplication;
import android.annotation.SuppressLint;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class contact_list implements Serializable {
private ArrayList<Contact> list;
private String Listname;
private int Security_level;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
private String AES = "AES";


    public void setListname(String listname) {
        Listname = listname;
    }

    public int getSecurity_level() {
        return Security_level;
    }

    public void setSecurity_level(int security_level) {
        Security_level = security_level;
    }

    public contact_list(String name, int sec, String password) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
    this.list = new ArrayList<Contact>();
    this.Listname = name;
    this.Security_level = sec;
    this.password = Encrypt(password);
}



    public String Encrypt(String passcode) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException {
        SecretKeySpec key = generateKey(passcode);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] nambyte = c.doFinal(passcode.getBytes());
        String encryptFirst = Base64.encodeToString(nambyte, Base64.DEFAULT);
        return encryptFirst;
    }

    private SecretKeySpec generateKey(String passcode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = passcode.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec sc = new SecretKeySpec(key, "AES");
        return sc;
    }

    public String Decrypt(String passcode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec key = generateKey(passcode);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodfirst = Base64.decode(this.password,Base64.DEFAULT);
        byte[] decodedf = c.doFinal(decodfirst);
        String df = new String(decodedf);
        return df;
    }

    public void decryptall(String password) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        for(int x = 0; x<= this.list.size()-1;x++){
            this.getList().get(x).Decrypt(password);
        }
    }

    public void encryptall(String password) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        for(int x = 0; x<= this.list.size()-1;x++){
            this.getList().get(x).Encrypt(password);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void encryptallbio(String password) throws Exception {
        //this.getList().get(0).EncryptBio(password);
        for(int x = 0; x<= this.list.size()-1;x++){
            this.getList().get(x).EncryptBio(password);
        }
    }

    public void decryptallbio(String password) throws Exception {
        //this.getList().get(0).DecryptBio(password);
        for(int x = 0; x<= this.list.size()-1;x++){
            this.getList().get(x).DecryptBio(password);
        }

    }

    public contact_list(String name, int sec){
        this.list = new ArrayList<Contact>();
        this.Listname = name;
        this.Security_level = sec;
    }

    public Boolean Unlock(String pass) {
        try {
            Decrypt(pass);
            if(pass.equals(Decrypt(pass))){
                return true;
            }
            else{
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;

        } catch (InvalidKeyException e) {
            return false;

        } catch (NoSuchPaddingException e) {
            return false;

        } catch (BadPaddingException e) {
            return false;

        } catch (UnsupportedEncodingException e) {
            return false;

        } catch (IllegalBlockSizeException e) {
            return false;

        }

    }

@RequiresApi(api = Build.VERSION_CODES.P)
public void add(Contact c, String password) throws Exception {

        this.list.add(c);

        if(this.Security_level == 0){

        }
        else if(this.Security_level == 1||this.Security_level ==3){
            this.encryptall(password);
        }
        else if(this.Security_level == 2||this.Security_level==4){
            this.encryptallbio(password);
        }

}

public String getListname(){
    return this.Listname;
}

public void remove(Contact c){
    this.list.remove(c);
}

public String toString(){
    String s = this.Listname +", " + Integer.toString(this.Security_level);
    return s;
}


    public ArrayList<Contact> getList() {
        return list;
    }

    public void setList(ArrayList<Contact> list) {
        this.list = list;
    }
}
