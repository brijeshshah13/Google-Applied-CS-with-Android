package com.example.vicky.scarnesdice;

import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private static int userTotalScore;
    private static int userTurnScore;
    private static int computerTotalScore;
    private static int computerTurnScore;
    Random random = new Random();
    public Button rollButton;
    public Button holdButton;
    public Button resetButton;
    public ImageView diceView;
    public TextView turnScoreText;
    public TextView turnScoreValue;
    public TextView userTotalScoreValue;
    public TextView computerTotalScoreValue;
    android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        diceView = (ImageView) findViewById(R.id.diceImage);
        turnScoreText = (TextView) findViewById(R.id.userTurnScoreText);
        turnScoreValue = (TextView) findViewById(R.id.userTurnScoreValue);
        userTotalScoreValue = (TextView) findViewById(R.id.userTotalScoreValue);
        computerTotalScoreValue = (TextView) findViewById(R.id.computerTotalScoreValue);

        turnScoreValue.setVisibility(View.INVISIBLE);
        turnScoreText.setVisibility(View.INVISIBLE);

        rollButtonImpl();
        holdButtonImpl();
        resetButtonImpl();
    }
    public void rollButtonImpl(){
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRollsTheDice();
            }
        });
    }
    public void holdButtonImpl(){
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTotalScore += userTurnScore;
                userTotalScoreValue.setText(String.valueOf(userTotalScore));
                userTurnScore = 0;
                turnScoreValue.setText(String.valueOf(userTurnScore));
                if(userTotalScore > 100){
                    Toast.makeText(getApplicationContext(),"You win!", Toast.LENGTH_LONG).show();
                }
                computerTurn();
            }
        });
    }
    public void resetButtonImpl(){
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTotalScore = 0;
                computerTotalScore = 0;
                userTurnScore = 0;
                turnScoreValue.setText(String.valueOf(userTurnScore));
                userTotalScoreValue.setText(String.valueOf(userTotalScore));
                computerTotalScoreValue.setText(String.valueOf(computerTotalScore));
            }
        });
    }
    public void userRollsTheDice(){
        int score = random.nextInt(6) + 1;
        diceImageChanger(score);
        if(score != 1){
            userTurnScore += score;
            turnScoreValue.setText(String.valueOf(userTurnScore));
        }
        else{
            userTurnScore = 0;
            turnScoreValue.setText(String.valueOf(userTurnScore));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },2000);
        }
        turnScoreText.setVisibility(View.VISIBLE);
        turnScoreValue.setVisibility(View.VISIBLE);
    }
    public void computerTurn(){
        final int score = random.nextInt(6) + 1;
        diceImageChanger(score);
        Boolean flag ;
        if(score != 1){
            computerTurnScore += score;
            turnScoreValue.setText(String.valueOf(computerTurnScore));

            flag=false;
            if(computerTurnScore >= 20){
                computerTotalScore += computerTurnScore;
                computerTurnScore = 0;
                flag=true;
                turnScoreValue.setText(String.valueOf(computerTurnScore));
                computerTotalScoreValue.setText(String.valueOf(computerTotalScore));
                if(computerTotalScore > 100){
                    Toast.makeText(getApplicationContext(),"Computer wins!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            computerTurnScore = 0;
            flag = true;
            turnScoreValue.setText(String.valueOf(computerTurnScore));
        }
        if(!flag)
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    rollButton.setEnabled(false);
                    holdButton.setEnabled(false);
                    computerTurn();
                }
            },1000);
        }
        else {
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),"Computer's turn has ended", Toast.LENGTH_SHORT).show();
        }
    }
    public void diceImageChanger(int randomValue){
        switch (randomValue){
            case 1:
                diceView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceView.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceView.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceView.setImageResource(R.drawable.dice6);
        }
    }
}
