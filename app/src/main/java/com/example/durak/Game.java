package com.example.durak;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    private Context context;
    private GameView gameView;
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> field = new ArrayList<>();
    private int height;
    private int width;
    private ArrayList<Player> players = new ArrayList<>();
    private int numberOfPlayers = 2;
    private int cardBitmapHeight;
    private int cardBitmapWidth;
    private Bitmap cardFrontBitmap;
    private Bitmap cardBackBitmap;
    private String trump;
    private Player attacker;
    private Player defender;
    private Player currentPlayer;
    private Card currentCard;
    private int fieldX;
    private int fieldY;
    private Button passButton;
    private Button takeButton;
    private TextView gameOverMessage;
    private Dialog menuPopup;

    public Game(Context context, Button passButton, Button takeButton, TextView gameOverMessage, Dialog menuPopup) {
        this.context = context;
        this.passButton = passButton;
        this.takeButton = takeButton;
        this.gameOverMessage = gameOverMessage;
        this.menuPopup = menuPopup;
    }

    public void newGame() {
        int scale = height / width;
        fieldX = width / 2;
        fieldY = height / 2 + height / 10;

        gameOverMessage.setText(" ");
        deck.clear();
        field.clear();
        initializeDeck();

        // TODO Refactor
        for (Card c : deck) {
            String path = c.getSuit() + String.valueOf(c.getRank());
            Resources resources = context.getResources();
            Bitmap originalCardBackBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.purple_back);
            Bitmap originalCardFrontBitmap = BitmapFactory.decodeResource(resources, resources.getIdentifier(path, "drawable", context.getPackageName()));
            cardBitmapHeight = originalCardFrontBitmap.getHeight() * 2 / 5 * scale;
            cardBitmapWidth = originalCardFrontBitmap.getWidth() * 2 / 5 * scale;

            cardBackBitmap = Bitmap.createScaledBitmap(originalCardBackBitmap, cardBitmapWidth, cardBitmapHeight, false);
            cardFrontBitmap = Bitmap.createScaledBitmap(originalCardFrontBitmap, cardBitmapWidth, cardBitmapHeight, false);

            c.setCurrentBitmap(cardBackBitmap);
            c.setCardFront(cardFrontBitmap);
            c.setCardBack(cardBackBitmap);

            c.setX(width / 2 - cardBitmapHeight / 2);
            c.setY(height / 4 - cardBitmapWidth / 2);
        }

        Collections.shuffle(deck);
        players.clear();
        initializePlayers(numberOfPlayers);
        Random random = new Random();
        int randomPlayerIndex =random.nextInt(players.size());
        currentPlayer = players.get(randomPlayerIndex);
        attacker = currentPlayer;

        for(int i = 0; i < players.size(); i++) {
            if(i != randomPlayerIndex) {
                defender = players.get(i);
            }
        }

        for (Player player : players) {
            transferCardsToPlayer(6, player);
        }

        Card lastCard = deck.get(deck.size() - 1);
        lastCard.flip();
        trump = lastCard.getSuit();
        // assign values to cards
        for (Card card : deck) {
            if (card.getSuit() == trump) {
                card.setValue(card.getValue() + 10);
            }
            Log.d("AI", card.getSuit() + card.getRank() + " has value of: " + card.getValue());
        }
        lastCard.setX(lastCard.getX() + (lastCard.getCurrentBitmap().getHeight() - lastCard.getCurrentBitmap().getWidth()) / 2);
        if (currentPlayer == players.get(1)) {
            easyComputerTurn(currentPlayer);
        }
        toggleButtons();
    }

    public void cardOnClick(int eventX, int eventY){

    }

    public boolean moveIsValid(Player player, Card card) {
        if (player != currentPlayer) {
            return false;
        }

        if (player == attacker) {
            if (field.size() == 0) {
                return true;
            }

            for (Card cardOnField : field) {
                if (cardOnField.getRank() == card.getRank()) {
                    return true;
                }
            }
            return false;
        }

        if (player == defender) {
            if (card.getSuit() == trump) {
                card.setValue(card.getRank());
                card.setValue(card.getValue() + 10);
                if(currentCard.getSuit() == trump) {
                    currentCard.setValue(currentCard.getRank());
                    currentCard.setValue(currentCard.getValue()+10);
                }
                if (card.getValue() > currentCard.getValue()) {
                    return true;
                }
            } else if (card.getSuit() == currentCard.getSuit()) {
                if (card.getValue() > currentCard.getValue()) {
                    return true;
                }
            }
        }

        return false;
    }


    public void pass() {
        if (!currentPlayer.isHuman()) {
            CharSequence text = "Pass";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        field.clear();
        swapAttackerAndDefender();
        swapCurrentPlayer();
        if (attacker.getHand().size() < 6) {
            transferCardsToPlayer(6 - attacker.getHand().size(), attacker);
        }
        if (defender.getHand().size() < 6) {
            transferCardsToPlayer(6 - defender.getHand().size(), defender);
        }
        gameView.invalidate();
    }

    public void take() {
        if (!currentPlayer.isHuman()) {
            CharSequence text = "Take";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        transferCardsFromFieldToPlayer(currentPlayer);
        field.clear();
        if (attacker.getHand().size() < 6) {
            transferCardsToPlayer(6 - attacker.getHand().size(), attacker);
        }
        swapCurrentPlayer();
        gameView.invalidate();
    }

    public void easyComputerTurn(Player player) {
        Card lowestValueCard = null;
        int lowestValue = 10000;
        ArrayList<Card> hand = player.getHand();
        if (player == attacker) {
            for (Card card : hand) {
                if (card.getSuit() == trump) {
                    card.setValue(card.getRank());
                    card.setValue(card.getValue() + 10);
                }
                int currentValue;
                if (moveIsValid(player, card)) {
                    currentValue = card.getValue();
                    if (currentValue < lowestValue) {
                        lowestValue = currentValue;
                        lowestValueCard = card;
                    }
                }
            }

            if (lowestValueCard == null) {
                Log.d("AI", "Pass");
                pass();
                return;

            } else {
                Log.d("AI", "Defend this bitch");
                transferCardToField(player, lowestValueCard);
                setCurrentCard(lowestValueCard);
                swapCurrentPlayer();
                return;
            }
        }

        if (player == defender) {
            for (Card card : hand) {
                if (card.getSuit() == trump) {
                    card.setValue(card.getValue() + 10);
                }

                if (card.getSuit() != trump) {
                }

                int currentValue;
                if (moveIsValid(player, card)) {
                    currentValue = card.getValue();
                    if (currentValue < lowestValue) {
                        lowestValue = currentValue;
                        lowestValueCard = card;
                    }
                }
            }

            if (lowestValueCard == null) {
                Log.d("AI", "Take");
                take();
                return;
            } else {
                Log.d("AI", "Keep attacking");
                transferCardToField(player, lowestValueCard);
                setCurrentCard(lowestValueCard);
                swapCurrentPlayer();
                return;
            }
        }
    }

    public void transferCardToField(Player player, Card card) {
        if (!player.isHuman()) {
            card.flip();
        }
        // TODO Refactor
        field.add(card);
        for (int i = 0; i < field.size(); i++) {
            if (i < 6) {
                if ((i % 2) == 0) { // even
                    Log.d("odd", "Even");
                    card.setX(fieldX - (cardBitmapWidth / 4 * (8 - 3 * i)));
                    card.setY(fieldY - cardBitmapWidth / 4 - cardBitmapHeight);
                } else { // odd
                    Log.d("odd", "Odd");
                    card.setX(fieldX - (cardBitmapWidth / 4 * (10 - i * 3)));
                    card.setY(fieldY - cardBitmapHeight);
                }
            } else {
                if ((i % 2) == 0) { // even
                    Log.d("odd", "Even");
                    card.setX(fieldX - (cardBitmapWidth / 4 * (8 - 3 * (i - 6))));
                    card.setY(fieldY + cardBitmapWidth / 4);
                } else { // odd
                    Log.d("odd", "Odd");
                    card.setX(fieldX - (cardBitmapWidth / 4 * (10 - (i - 6) * 3)));
                    card.setY(fieldY + cardBitmapWidth / 2);
                }
            }
        }
        player.getHand().remove(card);
        fanCards(player);
        gameView.invalidate();
    }

    public void initializeDeck() {
        int[] Ranks = {6, 7, 8, 9, 10, 11, 12, 13, 14};
        String[] Suits = {"d", "h", "s", "c"};

        for (int i = 0; i < Suits.length; i++) {
            for (int j = 0; j < Ranks.length; j++) {
                deck.add(new Card(Ranks[j], Suits[i]));
            }
        }
    }

    public void initializePlayers(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 2:
                players.add(new Player(width / 2, height - cardBitmapHeight, true, 0));
                players.add(new Player(width / 2, 0, false, 3));
                break;
        }
    }

    public void transferCardsFromFieldToPlayer(Player player) {
        for (int i = 0; i < field.size(); i++) {
            if (!player.isHuman()) {
                field.get(i).flip();
            }
            player.getHand().add(field.get(i));
            field.remove(field.get(i));
        }
        fanCards(player);
    }

    public void transferCardsToPlayer(int numberOfCards, Player player) {
        if (deck.size() == 0) {
            return;
        }
        if (deck.size() < numberOfCards) {
            numberOfCards = deck.size();
        }
        for (int i = 0; i < numberOfCards; i++) {
            Log.d("AI", deck.size() + " - deck size");
            if (deck.size() <= numberOfCards) {
                deck.get(deck.size() - 1).rotateBitmap(-90);
            }

            if (deck.size() < 36) {
                deck.get(0).rotateBitmap(-90);
            }

            if (player.isHuman() && deck.size() > 1) {
                deck.get(0).flip();
            }

            player.getHand().add(deck.get(0));
            deck.remove(0);

            if (deck.size() > 1) {
                deck.get(0).rotateBitmap(-90);
            }
        }
        fanCards(player);
        gameView.invalidate();
    }

    public void swapAttackerAndDefender() {
        Player initialAttacker = attacker;
        attacker = defender;
        defender = initialAttacker;
    }

    public void toggleButtons() {
        passButton.setClickable(false);
        takeButton.setClickable(false);
        passButton.setBackgroundColor(Color.parseColor("#4f4f4f"));
        takeButton.setBackgroundColor(Color.parseColor("#4f4f4f"));

        if (currentPlayer == players.get(0)) {
            if (currentPlayer == attacker && field.size() > 0) {
                passButton.setClickable(true);
                passButton.setBackgroundColor(Color.parseColor("#c60b3a"));
            }

            if (currentPlayer == defender) {
                takeButton.setClickable(true);
                takeButton.setBackgroundColor(Color.parseColor("#c60b3a"));
            }
        }
    }

    public void swapCurrentPlayer() {
        checkWinCondition();
        if (field.size() == 12) {
            pass();
        }
        if (currentPlayer == players.get(0)) {
            currentPlayer = players.get(1);
        } else {
            currentPlayer = players.get(0);
        }
        if (!currentPlayer.isHuman()) {
            easyComputerTurn(currentPlayer);
        }
        toggleButtons();
    }

    public void fanCards(Player player) {
        ArrayList<Card> hand = player.getHand();
        if (hand.size() > 0) {
            // Set X for the first card
            hand.get(0).setX(width / 2 - ((hand.size() - 1) * (cardBitmapWidth / 4) + cardBitmapWidth) / 2);

            // Set Y
            for (Card card : hand) {
                card.setY(player.getY());
            }

            // Set X for the rest of the cards based on X of first card
            for (int i = 1; i < hand.size(); i++) {
                hand.get(i).setX(hand.get(i - 1).getX() + cardBitmapWidth / 4);
            }
        }
    }

    public void checkWinCondition() {
        if (deck.size() == 0 && currentPlayer.getHand().size() == 0) {
            menuPopup.setCanceledOnTouchOutside(false);
            if (currentPlayer.isHuman()) {
                gameOverMessage.setText("You win!");
            } else {
                gameOverMessage.setText("You lose!");
            }
            menuPopup.show();
        }
    }

    public ArrayList<Card> getDeck() { return deck; }

    public ArrayList<Player> getPlayers() { return players; }

    public void setGameView(GameView view) { gameView = view; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public ArrayList<Card> getField() {
        return field;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public Player getAttacker() {
        return attacker;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    public Player getDefender() {
        return defender;
    }

    public void setDefender(Player defender) {
        this.defender = defender;
    }
}