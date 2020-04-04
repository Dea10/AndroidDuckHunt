package com.example.duckhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public Random random;

    public void initCountDownTimer() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                textViewTimer.setText("0s");
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        playerName = intent.getStringExtra(LoginActivity.EXTRA_NICK);

        bindUI();
        initScreen();
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

    public void onDuckClicked(View view) {
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
}
