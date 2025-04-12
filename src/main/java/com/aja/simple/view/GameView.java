package com.aja.simple.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import com.aja.simple.model.GameModel;
import com.aja.simple.component.Tile;
import com.aja.simple.core.BaseView;

public class GameView extends BaseView {
    private final GridPane grid = new GridPane();
    private final Label bombCounterLabel = new Label();
    private final Button backButton = new Button("Back to Menu");
    private VBox layout;
    private final GameModel model;

    public GameView(GameModel model) {
        this.model = model;
        initialize();   
    }

    public Button getBackButton() {
        return backButton;
    }

    @Override
    protected void initialize() {
        // Initialization is handled in the constructor
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < GameModel.SIZE; row++) {
            for (int col = 0; col < GameModel.SIZE; col++) {
                Tile tile = model.getTiles()[row][col];
                grid.add(tile, col, row);
            }
        }

        bombCounterLabel.setText("Bombs left: " + GameModel.BOMB_COUNT);
        bombCounterLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        layout = new VBox(20, bombCounterLabel, grid, backButton);
        layout.setAlignment(Pos.CENTER);

        scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());
    }

    public Label getBombCounterLabel() {
        return bombCounterLabel;
    }
}
