package com.example.duckhunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public EditText editTextPlayerName;
    public Button buttonStart;
    public Button buttonFirebaseTest;

    public String playerName;
    public FirebaseFirestore db;

    public static final String EXTRA_NICK = "EXTRA_NICK";
    public static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindUI();

        db = FirebaseFirestore.getInstance();
    }

    public void bindUI() {
        editTextPlayerName = findViewById(R.id.editTextPlayerName);
        buttonStart = findViewById(R.id.buttonStart);
        buttonFirebaseTest = findViewById(R.id.buttonFirebaseTest);
    }

    public void goToGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        playerName = editTextPlayerName.getText().toString();

        if (playerName.isEmpty()) {
            editTextPlayerName.setError("Add a player name");
        } else {
            intent.putExtra(EXTRA_NICK, playerName);
            startActivity(intent);
        }
    }

    public void firebaseTest(View view) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
