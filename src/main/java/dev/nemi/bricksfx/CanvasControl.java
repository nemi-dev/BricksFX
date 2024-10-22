package dev.nemi.bricksfx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class CanvasControl {

    @FXML
    private Canvas canvas;


    private AnimationTimer timer;

    @FXML
    public void initialize() {

        System.out.println("Canvas initialized.");
        var g = canvas.getGraphicsContext2D();

        timer = new Loop(g);

        timer.start();
    }

}
