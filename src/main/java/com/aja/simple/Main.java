package com.aja.simple;

import com.aja.simple.controller.MenuController;
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
        new MenuController(primaryStage); // MenuController manages scene transitions
    }

    public static void main(String[] args) {
        launch(args);
    }
}