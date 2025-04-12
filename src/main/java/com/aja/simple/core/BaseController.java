package com.aja.simple.core;

import javafx.stage.Stage;

public abstract class BaseController<M extends BaseModel, V extends BaseView> {
    protected M model;
    protected V view;
    private Stage stage;

    public BaseController(Stage stage) {
        this.stage = stage;
    }

    public void setScene() {
        stage.setScene(view.getScene());
    }

    public Stage getStage() {
        return stage;
    }

    protected abstract void bindEvents();
}
