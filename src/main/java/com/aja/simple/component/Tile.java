package com.aja.simple.component;

import javafx.scene.control.Button;

public class Tile extends Button {
    private final int row, col;
    private boolean isBomb = false;
    private boolean isOpened = false;
    private boolean isFlagged = false;
    private int adjacentBombs = 0;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        setPrefSize(40, 40);
        getStyleClass().add("tile");
    }

    public void open() {
        if (!isOpened && !isFlagged) {
            isOpened = true;
            setDisable(true);
            getStyleClass().add("tile-open");
            if (isBomb) {
                setText("B");
                setStyle("-fx-text-fill: red;");
            } else if (adjacentBombs > 0) {
                setText(String.valueOf(adjacentBombs));
            }
        }
    }

    public void toggleFlag() {
        if (!isOpened) {
            isFlagged = !isFlagged;
            setText(isFlagged ? "F" : "");
        }
    }

    // Getters and setters
    public int getRow() { 
        return row; 
    }

    public int getCol() {
        return col; 
    }

    public boolean isBomb() { 
        return isBomb; 
    }

    public void setBomb(boolean bomb) { 
        isBomb = bomb; 
    }

    public boolean isOpened() { 
        return isOpened; 
    }

    public boolean isFlagged() { 
        return isFlagged; 
    }

    public void setAdjacentBombs(int count) { 
        this.adjacentBombs = count; 
    }

    public int getAdjacentBombs() { 
        return adjacentBombs; 
    }
}
