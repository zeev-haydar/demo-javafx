package com.aja.simple.scene;

import com.aja.simple.Main;
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
    private final Button[][] tiles = new Button[SIZE][SIZE];
    private final boolean[][] bombs = new boolean[SIZE][SIZE];
    private final int[][] adjacentCounts = new int[SIZE][SIZE];
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
                Button tile = new Button();
                tile.getStyleClass().add("tile");
                tile.setPrefSize(40, 40);

                final int r = row;
                final int c = col;

                // Set mouse click event handler for each tile
                tile.setOnMouseClicked(e -> handleClick(r, c, e));

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

    private void handleClick(int row, int col, MouseEvent e) {
        Button tile = tiles[row][col];
    
        if (e.getButton().equals(MouseButton.SECONDARY)) {
            // System.out.println("Right-click detected on tile at (" + row + ", " + col + ")");
            if (!tile.isDisabled()) {
                if ("F".equals(tile.getText())) {
                    tile.setText("");
                    flagsPlaced--;
                } else {
                    tile.setText("F");
                    flagsPlaced++;
                }
                bombsLeft = BOMB_COUNT - flagsPlaced;
                bombCounterLabel.setText("Bombs left: " + bombsLeft);
            }
            return;
        }
    
        if (!tile.getText().equals("F") && !tile.isDisabled() && e.getButton().equals(MouseButton.PRIMARY)) {
            // System.out.println("Left-click detected on tile at (" + row + ", " + col + ")");
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

            // Ensure bomb isn't placed near the first clicked cell (3x3 safe zone)
            if (!bombs[r][c] && Math.abs(r - safeRow) > 1 || Math.abs(c - safeCol) > 1) {
                bombs[r][c] = true;
                placed++;
            }
        }
    }

    private void calculateAdjacentCounts() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (bombs[row][col]) continue;

                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr;
                        int nc = col + dc;
                        if (inBounds(nr, nc) && bombs[nr][nc]) {
                            count++;
                        }
                    }
                }
                adjacentCounts[row][col] = count;
            }
        }
    }

    private void openTile(int startRow, int startCol) {
        if (!inBounds(startRow, startCol) || tiles[startRow][startCol].isDisabled()) {
            return;
        }

        // Game over kalau bom yang dibuka
        if (bombs[startRow][startCol]) {
            tiles[startRow][startCol].setText("B");
            tiles[startRow][startCol].setStyle("-fx-text-fill: red;");
            tiles[startRow][startCol].getStyleClass().add("tile-open");
            tiles[startRow][startCol].setDisable(true);

            // Ungkap semua lokasi bom
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (bombs[row][col] && !tiles[row][col].isDisabled()) {
                        tiles[row][col].setText("💣");
                        tiles[row][col].getStyleClass().add("tile-open");
                        tiles[row][col].setDisable(true);
                    }
                }
            }

            // Dialog game over
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("You clicked on a bomb!");
            alert.showAndWait();
            
            // Balik ke menu
            mainApp.showMenu();
            return;
        }
    
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
    
        // Di sini kita menggunakan BFS untuk membuka semua tile yang berdekatan
        // dengan tile yang diklik, jika tile tersebut tidak memiliki bom di sekitarnya.
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0], col = pos[1];
    
            if (!inBounds(row, col) || tiles[row][col].isDisabled()) 
                continue;
            
            openedTiles++; // Increment jumlah tile yang dibuka
            Button tile = tiles[row][col];
            tile.getStyleClass().add("tile-open");
            tile.setDisable(true);
    
            int count = adjacentCounts[row][col];
            if (count > 0) {
                tile.setText(String.valueOf(count));
            } else {
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr != 0 || dc != 0) {
                            queue.offer(new int[]{row + dr, col + dc});
                        }
                    }
                }
            }
        }

        // Cek apabila semua tile yang bukan bom sudah dibuka
        // Jika semua tile yang bukan bom sudah dibuka, maka pemain menang
        if (openedTiles == SIZE * SIZE - BOMB_COUNT) {
            // Ungkap semua lokasi bom
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (bombs[row][col]) {
                        tiles[row][col].setText("B");
                        tiles[row][col].getStyleClass().add("tile-open");
                        tiles[row][col].setDisable(true);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You Win!");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations! You've cleared the board!");
            alert.showAndWait();

            mainApp.showMenu();
        }
    }
    

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
