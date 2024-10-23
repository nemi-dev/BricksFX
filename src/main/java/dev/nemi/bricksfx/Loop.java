package dev.nemi.bricksfx;

import dev.nemi.bricksfx.model.Bricks;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Loop extends AnimationTimer {
    private final GraphicsContext g;
    private final Bricks bricks;
    public Loop(GraphicsContext g, Bricks bricks) {
        this.g = g;
        this.bricks = bricks;
    }

    @Override
    public void handle(long now) {
        bricks.update(now);
        g.clearRect(0, 0, 800, 800);
        bricks.render(g);
    }
}
