package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText emailid, phoneNumber, password;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailid = findViewById(R.id.emailid);
        phoneNumber = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db= FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void gotoLoginPage(View view) {
        Intent  i =new Intent(this, login_activity.class);
        startActivity(i);
    }

    public void createAccount(View view) {
        String emailaddress = emailid.getText().toString().trim();
        String phonenumber = phoneNumber.getText().toString().trim();
        String userpassword = password.getText().toString().trim();


        if(!emailaddress.isEmpty()){
            if(phonenumber.length()==10){
                if(userpassword.length()>6){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please wait while Creating Account...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Map<String, String> v = new HashMap<>();
                    v.put("phoneNumber", phonenumber);
                    mAuth.createUserWithEmailAndPassword(emailaddress,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                String id =task.getResult().getUser().getUid().toString();
                                firebaseFirestore.collection("users").document(id).set(v);
                                Toast.makeText(SignUpActivity.this, "Account Creation Process Successful", Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(getApplicationContext(),login_activity.class);
                                startActivity(i);
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Account Already Exists", Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(getApplicationContext(),login_activity.class);
                                startActivity(i);
                                finish();
                            }

                        }
                    });

                }else {
                    Toast.makeText(this, "Please provide valid password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Please provide valid email id", Toast.LENGTH_SHORT).show();
        }

    }
}