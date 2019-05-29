package com.example.durak;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Card {
    private int x;
    private int y;
    private Bitmap currentBitmap;
    private String suit;
    private int rank;
    private int value;
    private Bitmap cardFront;
    private Bitmap cardBack;

    public Card(int rank, String suit) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank;
    }

    public void flip() {
        if (currentBitmap == cardFront) {
            currentBitmap = cardBack;
        } else {
            currentBitmap = cardFront;
        }
    }

    public void rotateBitmap(float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), matrix, true);
        cardBack = Bitmap.createBitmap(cardBack, 0, 0, cardBack.getWidth(), cardBack.getHeight(), matrix, true);
        cardFront = Bitmap.createBitmap(cardFront, 0, 0, cardFront.getWidth(), cardFront.getHeight(), matrix, true);
    }

    public int getRank() { return rank; }

    public String getSuit() { return suit; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public Bitmap getCurrentBitmap() { return currentBitmap; }

    public void setCurrentBitmap(Bitmap currentBitmap) { this.currentBitmap = currentBitmap; }

    public void setCardFront(Bitmap cardFront) { this.cardFront = cardFront; }

    public void setCardBack(Bitmap cardBack) { this.cardBack = cardBack; }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
