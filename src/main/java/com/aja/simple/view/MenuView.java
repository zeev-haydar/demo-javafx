package com.aja.simple.view;

import com.aja.simple.core.BaseView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;

public class MenuView extends BaseView {
    private final Button startButton = new Button("Start Game");

    public MenuView() {
        initialize();
    }

    @Override
    protected void initialize() {
        Text title = new Text("MINESWEEPER");
        title.getStyleClass().add("title");

        VBox layout = new VBox(20, title, startButton);
        layout.setAlignment(Pos.CENTER);

        scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
    }

    public Button getStartButton() {
        return startButton;
    }
}
