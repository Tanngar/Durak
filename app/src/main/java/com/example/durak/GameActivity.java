package com.example.durak;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameView gameView;
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
                menuPopup.setCanceledOnTouchOutside(true);
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
                    game.cardOnClick(event.getX(), event.getY());
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
