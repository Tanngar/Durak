package com.example.durak;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameView gameView;
    private Card selected;
    private Dialog menuPopup;
    private TextView gameOverMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
        menuPopup = new Dialog(this);
        menuPopup.setContentView(R.layout.menu_popup);
        gameOverMessage = menuPopup.findViewById(R.id.gameOverMessage);

        final Dialog rulesPopup = new Dialog(this);
        rulesPopup.setContentView(R.layout.rules_popup);

        Button backToMenuButton = rulesPopup.findViewById(R.id.backToMenu);
        Button passButton = findViewById(R.id.pass);
        Button takeButton = findViewById(R.id.take);
        Button menuButton = findViewById(R.id.menu);
        Button quitButton = menuPopup.findViewById(R.id.quit);
        Button rulesButton = menuPopup.findViewById(R.id.rules);

        passButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                game.pass();
            }
        });

        takeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                game.take();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                menuPopup.show();
            }
        });


        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.dismiss();
                finish();
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulesPopup.show();
            }
        });

        backToMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rulesPopup.dismiss();
            }
        });



        // TODO REFACTOR
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("AI", "Current player: "+game.getCurrentPlayer().isHuman()+" Attacker: "+game.getAttacker().isHuman()+" Defender: "+game.getDefender().isHuman());

                    if(selected != null) {
                        Log.d("deck", "Selected: " + selected.getSuit()+selected.getRank());
                    } else {
                        Log.d("deck", "Selected is null");
                    }
                    if (selected != null &&
                            event.getX() > selected.getX() &&
                            event.getX() < selected.getX() + selected.getCurrentBitmap().getWidth() &&
                            event.getY() > selected.getY() &&
                            event.getY() < selected.getY() + selected.getCurrentBitmap().getHeight()
                    ) {
                        if(game.moveIsValid(game.getCurrentPlayer(), selected)){
                            game.transferCardToField(game.getCurrentPlayer(), selected);
                            game.setCurrentCard(selected);
                            game.swapCurrentPlayer();
                            selected = null;
                        } else {
                            selected.setY(selected.getY() + selected.getCurrentBitmap().getHeight() / 2);
                            selected = null;
                        }
                    }
                    ArrayList<Card> hand = game.getPlayers().get(0).getHand();
                    for (int i = hand.size() - 1; i >= 0; i--) {
                        Card card = hand.get(i);

                        if (event.getX() > card.getX() &&
                                event.getX() < card.getX() + card.getCurrentBitmap().getWidth() &&
                                event.getY() > card.getY() &&
                                event.getY() < card.getY() + card.getCurrentBitmap().getHeight()
                        ) {
                            Log.d("deck", "Card clicked: " + card.getRank()+ card.getSuit());

                            if (selected != null) {
                                selected.setY(selected.getY() + selected.getCurrentBitmap().getHeight() / 2);
                            }

                            selected = card;
                            card.setY(card.getY() - card.getCurrentBitmap().getHeight() / 2);
                            break;
                        } else {
                            if (selected != null) {
                                selected.setY(selected.getY() + selected.getCurrentBitmap().getHeight() / 2);
                                selected = null;
                            }
                        }
                    }
                    gameView.invalidate();
                }
                return true;
            }
        });
        game = new Game(this, passButton, takeButton, gameOverMessage, menuPopup);
        game.setGameView(gameView);
        gameView.setGame(game);
    }

    public void newGame(View v) {
        game.newGame();
        menuPopup.dismiss();
    }
}
