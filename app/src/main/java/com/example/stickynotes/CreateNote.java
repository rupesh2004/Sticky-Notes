package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    EditText enoteTitle, enoteContent;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        enoteTitle = findViewById(R.id.NoteTitle);
        enoteContent = findViewById(R.id.NoteContent);
        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    public void viewNotes(View view) {
        Intent  i =new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void addNewNote(View view) {
        progressDialog.setMessage("Please wait while Adding Note...");
        progressDialog.setTitle("Add Note");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String noteTitle = enoteTitle.getText().toString().trim();
        String noteContent = enoteContent.getText().toString().trim();

        if(!noteTitle.isEmpty()){
            if(!noteContent.isEmpty()){
                DocumentReference documentReference = firebaseFirestore.collection("users").document(mUser.getUid()).collection("myNotes").document();
                Map<String , Object> note = new HashMap<>();
                note.put("title",noteTitle);
                note.put("content",noteContent);
                documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateNote.this, "Note Added Successfully..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNote.this, "Unable to Add the Note", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "Enter the Content in the Note", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Enter the Title to the Note", Toast.LENGTH_SHORT).show();
        }
    }
}