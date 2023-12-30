package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView t1;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        t1 = findViewById(R.id.fetchedData);

        db.collection("users")
                .document(mUser.getUid())
                .collection("myNotes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("MainActivity", "Error listening for data updates", e);

                            t1.setText("Error listening for data updates: " + e.getMessage());
                            return;
                        }

                        t1.setText("");

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String title = documentSnapshot.getString("title");
                            String content = documentSnapshot.getString("content");
                            t1.append("Title: " + title + "\n" + "Content: " + content + "\n\n");
                        }
                    }
                });
    }

    public void createNewNote(View view) {
        Intent i = new Intent(this, CreateNote.class);
        startActivity(i);
    }

    public void signout(View view) {
        mAuth.signOut();
        Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,login_activity.class);
        startActivity(i);
        finish();
    }
}
