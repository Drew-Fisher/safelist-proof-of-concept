package com.example.drew.myapplication;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MasterList extends Application implements Serializable {
    private ArrayList<contact_list> data;

    //private Context context = getBaseContext();
    public MasterList(){
        this.data = new ArrayList<contact_list>();
    }
    public void add(contact_list c){
        this.data.add(c);
    }

    public String check(){
        return this.data.get(0).toString();
    }

    public void save() throws IOException {
        FileOutputStream fout = openFileOutput("master.txt", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(this);
        oos.close();
        fout.close();
    }
    public void load() throws IOException, ClassNotFoundException {
        File f = new File(getFilesDir().getPath(),"master.txt");
        if(f.exists()){
            FileInputStream fin = openFileInput("master.txt");
            ObjectInputStream in = new ObjectInputStream(fin);
            MasterList temp = (MasterList) in.readObject();
            in.close();
            fin.close();
            this.setData(temp.getData());
        }else {
            
        }
    }
    public void deletelist(contact_list c){
        this.data.remove(c);
    }

    public ArrayList<contact_list> getData() {
        return data;
    }

    public void setData(ArrayList<contact_list> data) {
        this.data = data;
    }
}
