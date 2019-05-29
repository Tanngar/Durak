package com.example.durak;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainMenuActivity extends AppCompatActivity {
    private Dialog rulesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        // New game click listener
        rulesDialog = new Dialog(this);
        rulesDialog.setContentView(R.layout.rules_popup);

        Button newGameButton = findViewById(R.id.newGame);
        Button rulesButton = findViewById(R.id.rules);
        Button quitButton = findViewById(R.id.quit);
        Button backToMenuButton = rulesDialog.findViewById(R.id.backToMenu);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProgressBar progressBar = findViewById(R.id.progressBar1);
//                progressBar.setVisibility(View.VISIBLE  );
                startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rulesDialog.show();
            }
        });

        backToMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rulesDialog.dismiss();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
