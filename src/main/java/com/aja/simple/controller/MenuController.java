package com.aja.simple.controller;

import com.aja.simple.core.BaseController;
import com.aja.simple.model.MenuModel;
import com.aja.simple.view.MenuView;

import javafx.stage.Stage;

public class MenuController extends BaseController<MenuModel, MenuView> {

    public MenuController(Stage stage) {
        super(stage);
        this.model = new MenuModel();
        this.view = new MenuView();
        bindEvents();
        setScene();
    }

    @Override
    protected void bindEvents() {
        view.getStartButton().setOnAction(
            e -> {new GameController(getStage());}
        );
    }

    public MenuView getView() {
        return view;
    }
}
