package com.aja.simple.component;

import javafx.scene.control.Button;

public class Tile extends Button {
    private final int row;
    private final int col;
    private boolean hasBomb;
    private int adjacentBombs;
    private boolean isRevealed;
    private boolean isFlagged;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasBomb = false;
        this.adjacentBombs = 0;
        this.isRevealed = false;

        setPrefSize(40, 40);
        getStyleClass().add("tile");
    }

    public int getRow() {
         return row; 
    }

    public int getCol() { 
        return col;
    }

    public boolean hasBomb() {
        return hasBomb; 
    }

    public boolean isFlagged() { 
        return isFlagged; 
    }

    public void setBomb(boolean hasBomb) {
        this.hasBomb = hasBomb; 
    }

    public void setFlagged(boolean flagged) { 
        this.isFlagged = flagged; 
    }

    public int getAdjacentBombs() {
        return adjacentBombs; 
    }

    public void setAdjacentBombs(int count) { 
        this.adjacentBombs = count; 
    }

    public boolean isRevealed() { 
        return isRevealed; 
    }

    public void setRevealed(boolean revealed) { 
        this.isRevealed = revealed; 
    }
}