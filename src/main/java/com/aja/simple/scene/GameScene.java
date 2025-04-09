package com.aja.simple.scene;

import com.aja.simple.Main;
import com.aja.simple.component.Tile;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class GameScene {
    private Scene scene;
    private static final int SIZE = 12;
    private static final int BOMB_COUNT = 12;
    private final Tile[][] tiles = new Tile[SIZE][SIZE];
    private boolean firstClick = true;

    private Main mainApp; // Reference to the main application
    private int openedTiles = 0; // Counter for opened tiles
    private Label bombCounterLabel;
    private int flagsPlaced = 0;
    private int bombsLeft = BOMB_COUNT; // Counter for bombs left
    
    public GameScene(Main mainApp) {
        this.mainApp = mainApp;
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Tile tile = new Tile(row, col);
                tile.setOnMouseClicked(e -> handleClick(tile, e));
                tiles[row][col] = tile;
                grid.add(tile, col, row);
            }
        }

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> mainApp.showMenu());

        bombCounterLabel = new Label("Bombs Left: " + bombsLeft);
        bombCounterLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        VBox layout = new VBox(20, bombCounterLabel, grid, backButton);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private void handleClick(Tile tile, MouseEvent e) {
        int row = tile.getRow();
        int col = tile.getCol();

        if (e.getButton().equals(MouseButton.SECONDARY)) {
            if (!tile.isDisabled()) {
                if (tile.isFlagged()) {
                    tile.setText("");
                    tile.setFlagged(false);
                    flagsPlaced--;
                } else {
                    tile.setText("F");
                    tile.setFlagged(true);
                    flagsPlaced++;
                }
                bombsLeft = BOMB_COUNT - flagsPlaced;
                bombCounterLabel.setText("Bombs left: " + bombsLeft);
            }
            return;
        }

        if (!tile.isFlagged() && !tile.isDisabled() && e.getButton().equals(MouseButton.PRIMARY)) {
            if (firstClick) {
                placeBombs(row, col);
                calculateAdjacentCounts();
                firstClick = false;
            }

            openTile(row, col);
        }
    }

    private void placeBombs(int safeRow, int safeCol) {
        Random rand = new Random();
        int placed = 0;

        while (placed < BOMB_COUNT) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            Tile tile = tiles[r][c];

            if (!tile.hasBomb() && (Math.abs(r - safeRow) > 1 || Math.abs(c - safeCol) > 1)) {
                tile.setBomb(true);
                placed++;
            }
        }
    }

    private void calculateAdjacentCounts() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Tile tile = tiles[row][col];
                if (tile.hasBomb()) continue;

                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr;
                        int nc = col + dc;
                        if (inBounds(nr, nc) && tiles[nr][nc].hasBomb()) {
                            count++;
                        }
                    }
                }
                tile.setAdjacentBombs(count);
            }
        }
    }

    private void openTile(int startRow, int startCol) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];
            Tile tile = tiles[row][col];

            if (!inBounds(row, col) || tile.isDisabled()) 
                continue;

            tile.setDisable(true);
            tile.getStyleClass().add("tile-open");
            tile.setRevealed(true);
            openedTiles++;

            if (tile.hasBomb()) {
                tile.setText("B");
                tile.setStyle("-fx-text-fill: red;");
                revealAllBombs();
                showAlert("Game Over", "You clicked on a bomb!");
                mainApp.showMenu();
                return;
            }

            int count = tile.getAdjacentBombs();
            if (count > 0) {
                tile.setText(String.valueOf(count));
            } else {
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr != 0 || dc != 0) {
                            int nr = row + dr, nc = col + dc;
                            if (inBounds(nr, nc)) {
                                queue.offer(new int[]{nr, nc});
                            }
                        }
                    }
                }
            }
        }

        if (openedTiles == SIZE * SIZE - BOMB_COUNT) {
            revealAllBombs();
            showAlert("You Win!", "Congratulations! You've cleared the board!");
            mainApp.showMenu();
        }
    }

    private void revealAllBombs() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Tile tile = tiles[row][col];
                if (tile.hasBomb() && !tile.isDisabled()) {
                    tile.setText("B");
                    tile.getStyleClass().add("tile-open");
                    tile.setDisable(true);
                }
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
