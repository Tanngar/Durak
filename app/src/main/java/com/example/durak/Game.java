package com.example.durak;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private Context context;
    private GameView gameView;
    private ArrayList<Card> deck = new ArrayList<>();
    private int height;
    private int width;
    private ArrayList<Player> players = new ArrayList<>();
    private int numberOfPlayers = 2;
    private int cardBitmapHeight;
    private int cardBitmapWidth;
    private Bitmap cardFrontBitmap;
    private Bitmap cardBackBitmap;

    public Game(Context context) {
        this.context = context;
    }

    public void newGame() {
        int scale = height/width;
        initializeDeck();

        for(Card c : deck) {
//          Log.d("deck", "Rank: " + c.getRank() + " , Suit: " + c.getSuit() + " , Sizes: " + width + " " + height);
            String path =  c.getSuit() + String.valueOf(c.getRank());
            Resources resources = context.getResources();
            Bitmap originalCardBackBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.purple_back);
            Bitmap originalCardFrontBitmap = BitmapFactory.decodeResource(resources, resources.getIdentifier(path, "drawable", context.getPackageName()));
            cardBitmapHeight = originalCardFrontBitmap.getHeight()/10*scale;
            cardBitmapWidth = originalCardFrontBitmap.getWidth()/10*scale;

            cardBackBitmap = Bitmap.createScaledBitmap(originalCardBackBitmap, cardBitmapWidth, cardBitmapHeight, false);
            cardFrontBitmap = Bitmap.createScaledBitmap(originalCardFrontBitmap, cardBitmapWidth, cardBitmapHeight, false);

            c.setCurrentBitmap(cardBackBitmap);
            c.setCardFront(cardFrontBitmap);
            c.setCardBack(cardBackBitmap);

            c.setX(width / 2 - cardBitmapHeight / 2);
            c.setY(height / 4 - cardBitmapWidth / 2);
        }
        Collections.shuffle(deck);
        initializePlayers(numberOfPlayers);
        for(Player player : players) {
            transferCards(6, player);
        }

        Card lastCard = deck.get(deck.size()-1);
        deck.get(0).rotateBitmap(90);
        lastCard.flip();
        lastCard.setX(lastCard.getX() + (lastCard.getCurrentBitmap().getHeight() - lastCard.getCurrentBitmap().getWidth())/2);
    }

    public void initializeDeck() {
        int[] Ranks = { 6, 7, 8, 9, 10, 11, 12, 13, 14};
        String[] Suits = { "d", "h", "s", "c"};

        for(int i = 0; i < Suits.length; i++) {
            for(int j = 0; j < Ranks.length; j++ ) {
                deck.add(new Card(Ranks[j], Suits[i]));
            }
        }
    }

    public void initializePlayers(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 2:
                players.add(new Player(width/2, height-cardBitmapHeight, true, 0));
                players.add(new Player(width/2, 0, false, 3));
                break;
        }
    }

    public void transferCards(int numberOfCards, Player player) {
        for(int i = 0; i < numberOfCards; i++) {
            switch (player.getPosition()){
                case 0:
                    deck.get(0).rotateBitmap(0);
                    break;
                case 3:
                    deck.get(0).rotateBitmap(-180);
            }
            if(player.isHuman()) {
                deck.get(0).flip();
            }
            player.getHand().add(deck.get(0));
            deck.remove(0);
        }
        fanCards(player);
        gameView.invalidate();
    }

    public void fanCards(Player player) {
        ArrayList<Card> hand = player.getHand();
        if (hand.size()>0){
            // Set X for the first card
            hand.get(0).setX(width/2 - ((hand.size() - 1) * (cardBitmapWidth / 4) + cardBitmapWidth) / 2);

            // Set Y
            for(Card card : hand) {
                card.setY(player.getY());
            }

            // Set X for the rest of the cards based on X of first card
            for(int i=1;i<hand.size();i++){
                hand.get(i).setX(hand.get(i-1).getX() + cardBitmapWidth / 4);
            }
        }
    }

    public ArrayList<Card> getDeck() { return deck; }

    public ArrayList<Player> getPlayers() { return players; }

    public void setGameView(GameView view) { gameView = view; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

}