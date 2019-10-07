package com.example.drew.myapplication;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.P)
public class BiometricCallback extends BiometricPrompt.AuthenticationCallback {
    private  BiometricCallback biometricCallback;
    public BiometricCallback(BiometricCallback biometricCallback){
        this.biometricCallback = biometricCallback;
    }

}
