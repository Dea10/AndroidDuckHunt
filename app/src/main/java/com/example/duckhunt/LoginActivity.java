package com.example.duckhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public EditText editTextPlayerName;
    public Button buttonStart;
    public String playerName;

    public static final String EXTRA_NICK = "EXTRA_NICK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindUI();
    }

    public void bindUI() {
        editTextPlayerName = findViewById(R.id.editTextPlayerName);
        buttonStart = findViewById(R.id.buttonStart);
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
}
