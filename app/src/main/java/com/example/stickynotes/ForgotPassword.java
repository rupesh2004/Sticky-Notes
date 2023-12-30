package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email1;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email1=findViewById(R.id.emailid);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

    }

    public void resetPassword(View view) {
        progressDialog.setMessage("Please wait while sending email...");
        progressDialog.setTitle("Reset Password");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String emailAddress = email1.getText().toString().trim();
        if(!emailAddress.isEmpty()){
            mAuth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, "Reset password link has been sent to your registered email id", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),login_activity.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, "Email address not found! Please register again", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "Please provide Email Id", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoSignUpPage(View view) {
        Intent i = new Intent(this, login_activity.class);
        startActivity(i);
        finish();
    }
}