package com.example.vicky.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Declare all the variables and references for widgets
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

        // Map the references with appropriate widgets
        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        diceView = (ImageView) findViewById(R.id.diceImage);
        turnScoreText = (TextView) findViewById(R.id.userTurnScoreText);
        turnScoreValue = (TextView) findViewById(R.id.userTurnScoreValue);
        userTotalScoreValue = (TextView) findViewById(R.id.userTotalScoreValue);
        computerTotalScoreValue = (TextView) findViewById(R.id.computerTotalScoreValue);

        // Set the visibility of the "Turn score text" & "Turn score value" off
        turnScoreValue.setVisibility(View.INVISIBLE);
        turnScoreText.setVisibility(View.INVISIBLE);

        // Call the implementation method for Roll button
        rollButtonImpl();

        // Call the implementation method for Hold button
        holdButtonImpl();

        // Call the implementation method for Reset button
        resetButtonImpl();
    }

    // Implement the functionality of Roll button
    public void rollButtonImpl(){
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRollsTheDice();
            }
        });
    }

    // Implement the functionality of Hold button
    public void holdButtonImpl(){
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTotalScore += userTurnScore;
                userTotalScoreValue.setText(String.valueOf(userTotalScore));
                userTurnScore = 0;
                turnScoreValue.setText(String.valueOf(userTurnScore));

                // Display a Toast if user's score is greater than 100 as user wins
                // and reset all the scores
                if(userTotalScore > 100){
                    Toast.makeText(getApplicationContext(),"You win!", Toast.LENGTH_LONG).show();
                    resetWhenSomeoneWins();
                }

                // else the game continues with the computer's turn
                else
                    computerTurn();
            }
        });
    }

    // Implement the functionality of Reset button
    public void resetButtonImpl(){
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set all the scores to 0
                userTotalScore = 0;
                computerTotalScore = 0;
                userTurnScore = 0;
                computerTurnScore = 0;

                // Update the label texts
                turnScoreValue.setText(String.valueOf(userTurnScore));
                userTotalScoreValue.setText(String.valueOf(userTotalScore));
                computerTotalScoreValue.setText(String.valueOf(computerTotalScore));
            }
        });
    }

    // Implement the functionality for resetting all the scores
    // when either the user or the computer wins
    public void resetWhenSomeoneWins(){

        // Set all the scores to 0
        userTotalScore = 0;
        computerTotalScore = 0;
        userTurnScore = 0;
        computerTurnScore = 0;

        // Update the label texts
        turnScoreValue.setText(String.valueOf(userTurnScore));
        userTotalScoreValue.setText(String.valueOf(userTotalScore));
        computerTotalScoreValue.setText(String.valueOf(computerTotalScore));
    }

    // Implement the algorithm for updating appropriate scores
    //  at the right time when user rolls the dice
    public void userRollsTheDice(){

        // Generate a random number for the dice
        int score = random.nextInt(6) + 1;

        // Update the display to reflect the rolled value on the dice
        diceImageChanger(score);

        // If the score is not equal to 1, update the turn score value and
        // the displayed turn score
        if(score != 1){
            userTurnScore += score;
            turnScoreValue.setText(String.valueOf(userTurnScore));
        }
        // else, i.e, if score = 1, update the "Turn score Value = 0" &
        // the displayed turn score & then it's computer's turn
        else{
            userTurnScore = 0;
            turnScoreValue.setText(String.valueOf(userTurnScore));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },1000);
        }

        // Set the visibility of the "Turn score text" & "Turn score value" on
        turnScoreText.setVisibility(View.VISIBLE);
        turnScoreValue.setVisibility(View.VISIBLE);
    }

    // Implement the algorithm for updating appropriate scores
    //  at the right time when computer rolls the dice
    public void computerTurn(){

        // Generate a random number for the dice
        final int score = random.nextInt(6) + 1;

        // Update the display to reflect the rolled value on the dice
        diceImageChanger(score);

        // flag is maintained for monitoring when the time when the
        // computer's turn end. If flag = true, computer's turn has
        // ended and if flag = false, either the computer has not got
        // a 1 on the dice yet or the "Turn score Value < 20"
        Boolean flag ;

        // If the score is not equal to 1, update the turn score value and
        // the displayed turn score
        if(score != 1){
            computerTurnScore += score;
            turnScoreValue.setText(String.valueOf(computerTurnScore));

            // By default flag = false
            flag = false;

            // Checking if the computer's turn score is >= 20
            if(computerTurnScore >= 20){
                computerTotalScore += computerTurnScore;
                computerTurnScore = 0;
                flag = true;
                turnScoreValue.setText(String.valueOf(computerTurnScore));
                computerTotalScoreValue.setText(String.valueOf(computerTotalScore));

                // Display a Toast if computer's score is greater than 100 as computer wins
                // and reset all the scores
                if(computerTotalScore > 100){
                    Toast.makeText(getApplicationContext(),"Computer wins!", Toast.LENGTH_LONG).show();
                    resetWhenSomeoneWins();
                }
            }
        }

        // else, i.e, if score = 1, update the "Turn score Value = 0" &
        // the displayed turn score & then it's user's turn
        else{
            flag = true;
            computerTurnScore = 0;
            turnScoreValue.setText(String.valueOf(computerTurnScore));
        }
        if(!flag)
        {
            // Delaying each throw of the dice of computer's turn by 1 sec
            // to update the display to reflect all the rolled values on the dice
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    // Disable the Roll and Hold buttons when it's computer's turn
                    rollButton.setEnabled(false);
                    holdButton.setEnabled(false);
                    computerTurn();
                }
            },1000);
        }
        else {
            // Enable the Roll and Hold buttons when it's user's turn
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),"Computer's turn has ended", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to update the display to reflect the rolled value on the dice
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
