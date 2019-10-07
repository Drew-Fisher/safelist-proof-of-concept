package com.example.drew.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

public class Biometrics {


    public Boolean isEnabled(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    public static Boolean issdkEnabled(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static Boolean isHardware(Context c){
        FingerprintManagerCompat f = FingerprintManagerCompat.from(c);
        return f.isHardwareDetected();
    }

    public static Boolean isfingerPrint(Context c){
        FingerprintManagerCompat fig = FingerprintManagerCompat.from(c);
        return fig.hasEnrolledFingerprints();
    }

    public static Boolean isPermission(Context c){
        return ActivityCompat.checkSelfPermission(c, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;
    }

}
