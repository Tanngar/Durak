package com.example.durak;

import java.util.ArrayList;

public class Player {
    private int x;
    private int y;
    private ArrayList<Card> hand = new ArrayList<>();
    private boolean human;
    private int position;

    public Player(int x, int y, boolean human, int position) {
        this.x = x;
        this.y = y;
        this.human = human;
        this.position = position;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public ArrayList<Card> getHand() { return hand; }

    public boolean isHuman() {
        return human;
    }

    public int getPosition() {
        return position;
    }
}
