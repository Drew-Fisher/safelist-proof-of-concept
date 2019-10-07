package com.example.drew.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cloudLogin extends AppCompatActivity {
    private EditText email,password;
    private Button login,signup,back;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_login);

        email = findViewById(R.id.cloud_email_login);
        password = findViewById(R.id.cloud_password_login);
        login = findViewById(R.id.cloudLogin);
        signup = findViewById(R.id.cloudSignup);
        back = findViewById(R.id.cloud_login_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ListOfListActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin(email.getText().toString(),password.getText().toString());

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), cloud_signup.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null){
            Intent I = new Intent(getBaseContext(),viewCloudList.class);
            I.putExtra("userID",currentUser.getUid().toString());
            startActivity(I);
        }

    }


    public void signin(String entered_email,String entered_password){
        mAuth.signInWithEmailAndPassword(entered_email, entered_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent I = new Intent(getBaseContext(),viewCloudList.class);
                            I.putExtra("userID",user.getUid().toString());
                            startActivity(I);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(cloudLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
