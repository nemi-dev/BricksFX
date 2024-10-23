package dev.nemi.bricksfx;

import dev.nemi.bricksfx.model.Bricks;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class CanvasControl {

    @FXML
    private Canvas canvas;


    private AnimationTimer timer;

    @FXML
    public void initialize() {

        var g = canvas.getGraphicsContext2D();
        timer = new Loop(g, new Bricks());
        timer.start();
    }

}
