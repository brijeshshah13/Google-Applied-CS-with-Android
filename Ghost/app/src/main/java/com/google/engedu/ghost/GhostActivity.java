/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {

    // Declare all the variables and references for widgets
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static String wordFragment;
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private static TextView ghostText;
    private static TextView gameStatus;
    private static Button challengeButton;
    private static Button restartButton;
    private static String wordSelectedByComputer;
    Handler handler = new Handler();
    private String yourWord = null;
    int whoEndsFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        // Map the references with appropriate widgets
        ghostText = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        challengeButton = (Button) findViewById(R.id.challengeButton);
        restartButton = (Button) findViewById(R.id.restartButton);

        // Implement the functionality of Restart button
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(null);
                createToast("Game restarts...",100);
            }
        });

        // Implement the functionality of Challenge button
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordFragment.length() >= 4 ){
                    yourWord = dictionary.getAnyWordStartingWith(wordFragment);
                    if(yourWord == "noWord")
                        createToast("You Win! No such word",1000);
                    else if(yourWord == "sameAsPrefix")
                        createToast("You Win! Computer ended the word",1000);
                    else
                        Toast.makeText(getApplication(),"Computer wins. The word was : " + wordSelectedByComputer,Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplication(),"You challenged too early. Computer wins. Word is still less then 4 characters",Toast.LENGTH_LONG).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onStart(null);
                    }
                },1000);
            }
        });

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            createToast("Could not load dictionary",500);
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        whoEndsFirst = userTurn ? 1:0;
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        gameStatus.setText(COMPUTER_TURN);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordFragment = String.valueOf(ghostText.getText());
                //wordSelectedByComputer = dictionary.getAnyWordStartingWith(wordFragment);
                wordSelectedByComputer = dictionary.getGoodWordStartingWith(wordFragment, whoEndsFirst);
                if(wordSelectedByComputer == "noWord"){
                    createToast("Computer wins! No such word",500);
                    onStart(null);
                }
                else if(wordSelectedByComputer == "sameAsPrefix"){
                    createToast("Computer wins! You ended the word",1000);
                    onStart(null);
                }
                else if(wordSelectedByComputer.length() != 1){
                    wordFragment += wordSelectedByComputer.charAt(wordFragment.length());
                    ghostText.setText(String.valueOf(wordFragment));
                }
                else {
                    wordFragment += wordSelectedByComputer;
                    ghostText.setText(String.valueOf(wordFragment));
                }

                // Do computer turn stuff then make it the user's turn again
                userTurn = true;
                gameStatus.setText(USER_TURN);
            }
        },1000);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        pressedKey = Character.toLowerCase(pressedKey);

        if(pressedKey >= 'a' && pressedKey <= 'z'){
            wordFragment = String.valueOf(ghostText.getText());
            wordFragment += pressedKey;
            ghostText.setText(wordFragment);
            createToast("Computer's turn",100);
            computerTurn();
            return true;
        }
        else
            return super.onKeyUp(keyCode, event);
    }

    public void createToast(String message, int time){
        Toast.makeText(getApplication(),message,time).show();
    }
}
