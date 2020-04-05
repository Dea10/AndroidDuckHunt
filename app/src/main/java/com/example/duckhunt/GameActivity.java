package com.example.duckhunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public TextView textViewNick;
    public TextView textViewCounter;
    public TextView textViewTimer;

    public ImageView imageViewDuck;
    public ImageView imageViewCounter;
    public ImageView imageViewTimer;

    public String playerName;
    public Integer huntedDucksCounter = 0;
    public Integer screenWidth;
    public Integer screenHeight;
    public Boolean gameOver = false;

    public Random random;

    public FirebaseFirestore db;

    public final String TAG = GameActivity.class.getName();

    // *********** ANDROID METHODS ***********
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        playerName = intent.getStringExtra(LoginActivity.EXTRA_NICK);

        db = FirebaseFirestore.getInstance();

        bindUI();
        initScreen();
        resetDuck();
        initCountDownTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewNick.setText(playerName);
    }

    public void bindUI() {
        textViewNick = findViewById(R.id.textViewNick);
        textViewCounter = findViewById(R.id.textViewCounter);
        textViewTimer = findViewById(R.id.textViewTimer);

        imageViewDuck = findViewById(R.id.imageViewDuck);
        imageViewCounter = findViewById(R.id.imageViewCounter);
        imageViewTimer = findViewById(R.id.imageViewTimer);
    }

    // *********** GAME METHODS ***********

    public void initScreen() {
        // 1. Obtener el tamaño de la pantalla del dispositivo.
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // 2. Inicializar random
        random = new Random();
    }

    public void onDuckClicked(View view) {
        if (!gameOver) {
            huntedDucksCounter++;
            textViewCounter.setText(String.valueOf(huntedDucksCounter));

            // show hunted duck
            imageViewDuck.setImageResource(R.drawable.duck_clicked);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageViewDuck.setImageResource(R.drawable.duck);
                    resetDuck();
                }
            }, 500);
        }
    }

    private void resetDuck() {
        Integer min = 0;
        Integer maxWidth = screenWidth - imageViewDuck.getWidth();
        Integer maxHeight = screenHeight - imageViewDuck.getHeight();

        // Generamos 2 random
        Integer randomX = random.nextInt((maxWidth - min) + 1 + min);
        Integer randomY = random.nextInt((maxHeight - min) + 1 + min);

        //Usamos random para mover el pato
        imageViewDuck.setX(randomX);
        imageViewDuck.setY(randomY);
    }

    public void initCountDownTimer() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                textViewTimer.setText("0s");
                gameOver = true;
                showGameOver();
                sendDataToFirebase();
            }
        }.start();
    }

    public void showGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                resetGame();
            }
        });
        builder.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                finish();
            }
        });

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Ducks hunted: " + huntedDucksCounter).setTitle("Game Over");

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void resetGame() {
        textViewCounter.setText("0");
        textViewTimer.setText("60s");

        gameOver = false;
        huntedDucksCounter = 0;
        resetDuck();
        initCountDownTimer();
    }

    public void sendDataToFirebase() {
        UserPunctuation userPunctuation = new UserPunctuation(playerName, huntedDucksCounter);
        db.collection("ranking").add(userPunctuation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
        });
    }
}
