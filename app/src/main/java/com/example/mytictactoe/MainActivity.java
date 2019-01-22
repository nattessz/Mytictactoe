package com.example.mytictactoe;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    Animation animMoveUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        //Gombok beállítása, bejárjuk a tömbött
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                animMoveUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_up);
                buttons[i][j].startAnimation(animMoveUp);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            resetGame();
            }
        });
    }
    @Override
    public void onClick(View v){
        if(!((Button) v).getText().toString().equals("")){
            return;
        }

        if (player1Turn){
            ((Button)v).setText("X");
        } else {
            ((Button)v).setText("O");
        }

        roundCount++;

        if(checkForWin()){
            if (player1Turn){
                player1Wins();
            }
            else{
                player2Wins();
            }
        } else if(roundCount == 9){
            draw();
        }else {
            player1Turn = !player1Turn;
        }

    }

    //Ellenőrizük, hogy ki győzött
    private boolean checkForWin(){
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i=0; i<3; i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
            return true;
            }
        }
        for (int i=0; i<3; i++){
            if(field[0][i].equals(field[1][i])&& field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
      }
        if(field[0][0].equals(field[1][1])&& field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            return true;
        }
        if(field[0][2].equals(field[1][1])&& field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            return true;
        }
        return false;
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText(this,"Első játékos nyert",Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.jatekos);
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this,"Második játékos nyert",Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.jatokos);
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void draw() {
        Toast.makeText(this,"Döntetlen!",Toast.LENGTH_SHORT).show();
        resetBoard();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.dontetlen);
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Játékos X: " + player1Points);
        textViewPlayer2.setText("Játékos O: " + player2Points);
    }

    //alapállapotba állítás
    private void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                animMoveUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_up);
                buttons[i][j].startAnimation(animMoveUp);
            }
        }
        roundCount=0;
        player1Turn=true;
    }

    private  void resetGame(){
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ujjjatek);
        mp.start();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount",roundCount);
        outState.putInt("player1Points",player1Points);
        outState.putInt("player2Points",player2Points);
        outState.putBoolean("player1Turn",player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}