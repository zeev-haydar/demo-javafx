package com.aja.simple.controller;

import com.aja.simple.model.GameModel;
import com.aja.simple.component.Tile;
import com.aja.simple.core.BaseController;
import com.aja.simple.view.GameView;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Queue;

public class GameController extends BaseController<GameModel, GameView> {
    private boolean firstClick = true;
    private int openedTiles = 0;
    private int flagsPlaced = 0;

    public GameController(Stage stage) {
        super(stage);
        this.model = new GameModel();
        this.view = new GameView(model);

        // Initialize the back button for returning to the menu
        Button backButton = view.getBackButton();
        backButton.setOnAction(e -> {
            // Switch back to the menu scene when back is clicked
            new MenuController(stage);
        });

        // Bind mouse click events to the tiles
        for (Tile[] row : model.getTiles()) {
            for (Tile tile : row) {
                tile.setOnMouseClicked(e -> handleClick(tile, e));
            }
        }

        setScene(); // Set the GameView scene when the controller is initialized
    }

    @Override
    protected void bindEvents() {
        // Any additional event bindings specific to the game can be added here
    }

    private void handleClick(Tile tile, MouseEvent e) {
        if (tile.isOpened()) return;

        if (e.getButton() == MouseButton.SECONDARY) {
            // Handle right-click for flagging
            tile.toggleFlag();
            flagsPlaced += tile.isFlagged() ? 1 : -1;
            int bombsLeft = GameModel.BOMB_COUNT - flagsPlaced;
            view.getBombCounterLabel().setText("Bombs left: " + bombsLeft);
        } else if (e.getButton() == MouseButton.PRIMARY && !tile.isFlagged()) {
            if (firstClick) {
                model.placeBombs(tile.getRow(), tile.getCol());
                model.calculateAdjacentCounts();
                firstClick = false;
            }

            if (tile.isBomb()) {
                revealAllBombs();
                showAlert("Game Over", "You clicked on a bomb!");
                // Transition back to the menu after game over
                new MenuController(getStage());
            } else {
                openTile(tile.getRow(), tile.getCol());
                if (openedTiles == GameModel.SIZE * GameModel.SIZE - GameModel.BOMB_COUNT) {
                    revealAllBombs();
                    showAlert("You Win!", "Congratulations! You've cleared the board!");
                    // Transition back to the menu after win
                    new MenuController(getStage());
                }
            }
        }
    }

    private void openTile(int row, int col) {
        Queue<Tile> queue = new LinkedList<>();
        queue.offer(model.getTiles()[row][col]);

        while (!queue.isEmpty()) {
            Tile tile = queue.poll();

            if (tile.isOpened() || tile.isFlagged()) continue;

            tile.open();
            openedTiles++;

            if (tile.getAdjacentBombs() == 0) {
                // Add neighboring tiles to the queue for opening if there are no adjacent bombs
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = tile.getRow() + dr;
                        int nc = tile.getCol() + dc;
                        if (model.inBounds(nr, nc)) {
                            queue.offer(model.getTiles()[nr][nc]);
                        }
                    }
                }
            }
        }
    }

    private void revealAllBombs() {
        for (Tile[] row : model.getTiles()) {
            for (Tile tile : row) {
                if (tile.isBomb()) {
                    tile.open();
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
}
