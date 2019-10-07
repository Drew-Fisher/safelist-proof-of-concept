package com.example.drew.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cloud_signup extends AppCompatActivity {
    private EditText email,password,confirm_password;
    private Button back,signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_signup);

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirm_password = findViewById(R.id.signup_confirm_password);
        back = findViewById(R.id.cloud_signup_cancel);
        signup = findViewById(R.id.cloud_signup_attempt);
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),cloudLogin.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_email = email.getText().toString().trim();
                String e_pass = password.getText().toString().trim();
                String e_c_pass = confirm_password.getText().toString().trim();
                if(e_pass.equals(e_c_pass)){
                    signup(e_email,e_pass);
                }
                else {
                    Toast.makeText(cloud_signup.this, "dont match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signup(String entered_email, String entered_password){
        mAuth.createUserWithEmailAndPassword(entered_email, entered_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            Intent i = new Intent(getBaseContext(),cloudLogin.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(cloud_signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}
