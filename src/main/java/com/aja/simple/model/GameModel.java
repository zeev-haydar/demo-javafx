package com.aja.simple.model;

import java.util.Random;

import com.aja.simple.component.Tile;
import com.aja.simple.core.BaseModel;

public class GameModel extends BaseModel {
    public static final int SIZE = 12;
    public static final int BOMB_COUNT = 12;
    private final Tile[][] tiles = new Tile[SIZE][SIZE];

    public GameModel() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void placeBombs(int safeRow, int safeCol) {
        Random rand = new Random();
        int placed = 0;

        while (placed < BOMB_COUNT) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);

            if (!tiles[r][c].isBomb() && (Math.abs(r - safeRow) > 1 || Math.abs(c - safeCol) > 1)) {
                tiles[r][c].setBomb(true);
                placed++;
            }
        }
    }

    public void calculateAdjacentCounts() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (tiles[row][col].isBomb()) continue;

                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr, nc = col + dc;
                        if (inBounds(nr, nc) && tiles[nr][nc].isBomb()) {
                            count++;
                        }
                    }
                }
                tiles[row][col].setAdjacentBombs(count);
            }
        }
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
