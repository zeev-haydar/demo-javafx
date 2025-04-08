package com.aja.simple.scene;
import com.aja.simple.Main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuScene {
    private Scene scene;

    public MenuScene(Main mainApp) {
        // Game title text
        Text title = new Text("MINESWEEPER");
        title.getStyleClass().add("title"); // add class name for CSS

        // Start Game Button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> mainApp.startGame());

        VBox layout = new VBox(20, title, startButton);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}