package com.example.durak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameView extends View {
    private Game game;
    private int height;
    private int width;
    private boolean gameInitiliazed = false;
    // testing shit, remove later

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }


    public GameView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        height = getHeight();
        width = getWidth();
        game.setHeight(height);
        game.setWidth(width);

        if(!gameInitiliazed) {
            gameInitiliazed = true;
            game.newGame();
        }

        Paint paint = new Paint();
        canvas.drawColor(Color.parseColor("#033b32"));

        if(game.getDeck().size()>0){
            Card firstCard = game.getDeck().get(0);
            Card lastCard = game.getDeck().get(game.getDeck().size()-1);
            canvas.drawBitmap(lastCard.getCurrentBitmap(), lastCard.getX(), lastCard.getY(), paint);
            canvas.drawBitmap(firstCard.getCurrentBitmap(), firstCard.getX(), firstCard.getY(), paint);
        }

        for(Card cardOnField : game.getField()){
            canvas.drawBitmap(cardOnField.getCurrentBitmap(), cardOnField.getX(), cardOnField.getY(), paint);
        }

        for(Player player : game.getPlayers()) {
            for(Card card : player.getHand()) {
                canvas.drawBitmap(card.getCurrentBitmap(), card.getX(), card.getY(), paint);
            }
        }


        super.onDraw(canvas);
    }


    public void setGame(Game game) { this.game = game; }

}
