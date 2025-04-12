package com.aja.simple.core;

import javafx.scene.Scene;

public abstract class BaseView {
    protected Scene scene;

    public Scene getScene() {
        return scene;
    }

    // define method to initialize layout if needed
    protected abstract void initialize();
}
