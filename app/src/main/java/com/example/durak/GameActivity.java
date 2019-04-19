package com.example.durak;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameView gameView;
    private Card selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.gameView);
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ArrayList<Card> hand = game.getPlayers().get(0).getHand();
                    for (int i = hand.size() - 1; i >= 0; i--) {
                        Card card = hand.get(i);
                        if (event.getX() > card.getX() &&
                                event.getX() < card.getX() + card.getCurrentBitmap().getWidth() &&
                                event.getY() > card.getY() &&
                                event.getY() < card.getY() + card.getCurrentBitmap().getHeight()
                        ) {
                            if (selected != null) {
                                selected.setY(selected.getY() + selected.getCurrentBitmap().getHeight() / 2);
                            }
                            card.setY(card.getY() - card.getCurrentBitmap().getHeight() / 2);
                            selected = card;
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
        game = new Game(this);
        game.setGameView(gameView);
        gameView.setGame(game);
    }

//    Return to main menu button
//    private void configureBackToMainMenuButton(){
//        Button backToMainMenuButton = (Button) findViewById(R.id.newGame);
//        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
}
