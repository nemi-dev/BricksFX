package dev.nemi.bricksfx;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Loop extends AnimationTimer {
    private final GraphicsContext g;
    public Loop(GraphicsContext g) {
        this.g = g;
    }

    @Override
    public void handle(long l) {
        g.clearRect(0, 0, 800, 800);
    }
}
