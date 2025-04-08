package com.aja.simple;

import com.aja.simple.scene.GameScene;
import com.aja.simple.scene.MenuScene;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private static final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Minesweeper");
        showMenu();
        primaryStage.show();
    }

    public void showMenu() {
        MenuScene menu = new MenuScene(this);
        primaryStage.setScene(menu.getScene());
    }

    public void startGame() {
        GameScene game = new GameScene(this);
        primaryStage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}