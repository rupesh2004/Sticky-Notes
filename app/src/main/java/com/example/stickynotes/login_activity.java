package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_activity extends AppCompatActivity {

    EditText email1, password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email1 = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);

    }

    protected void onStart() {
        super.onStart();
        int flag=getIntent().getIntExtra("flag",0);
        if(flag==1) {
            return ;
        }
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if (firebaseUser!=null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (firebaseUser==null){
            mAuth.signOut();
        }
    }



    public void logInApp(View view) {
        String emailId = email1.getText().toString().trim();
        String password1 = password.getText().toString().trim();

        if (!emailId.isEmpty()) {
            if (password1.length() > 6) {
                progressDialog.setMessage("Please wait while Login Complete...");
                progressDialog.setTitle("Login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(emailId, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                                Toast.makeText(login_activity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                        } else {
                            Toast.makeText(login_activity.this, "Login Failed: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please provide a valid Password (at least 7 characters)", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please provide an Email Address", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoSignUpPage(View view) {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void forgotPassword(View view) {
        Intent i = new Intent(this, ForgotPassword.class);
        startActivity(i);
    }

    public void resetPassword(View view) {
    }
}