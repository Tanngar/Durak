package com.example.durak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
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
