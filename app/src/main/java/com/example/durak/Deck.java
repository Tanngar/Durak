package com.example.durak;

import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int[] Ranks = { 6, 7, 8, 9, 10, 11, 12, 13, 14};
    private String[] Suits = { "d", "h", "s", "c"};

    public Deck() {
        cards = new ArrayList<>();
        for(int i = 0; i < Suits.length; i++) {
            for(int j = 0; j < Ranks.length; j++ ) {
                cards.add(new Card(Ranks[j], Suits[i]));
            }
        }
    }

    public ArrayList<Card> getCards() { return cards; }
}
