package dev.nemi.bricksfx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.LinkedList;

public class Bricks {
    public static final int CELL_SIZE = 5;
    public static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;
    public static final int COUNT_COLUMN_DIRECTION = COUNT_ROW_DIRECTION / 2;

    private int[][] matrix;
    private LinkedList<Ball> balls;
    private long last = 0L;

    public Bricks() {
        init();
    }

    public void init() {
        matrix = new int[COUNT_COLUMN_DIRECTION][COUNT_ROW_DIRECTION];
        balls = new LinkedList<>();

        for (int i = 0; i < COUNT_COLUMN_DIRECTION; i++) {
            for (int j = 0; j < COUNT_ROW_DIRECTION; j++) {
                matrix[i][j] = 1;
            }
        }
    }

    void addBall(float x, float y, float dx, float dy) {
        balls.add(new Ball(x, y, dx, dy));
    }

    public void update(long now) {

        for (Ball ball : balls) {
            ball.update(now, last);
        }

        last = now;

    }

    public void render(GraphicsContext g) {
        g.setFill(Color.rgb(0, 128, 255, 0.5));
        g.setStroke(Color.rgb(0, 128, 255, 1.0));
        for (int r = 0; r < COUNT_COLUMN_DIRECTION; r += 1) {
            for (int c = 0; c < COUNT_ROW_DIRECTION; c += 1) {
                if (matrix[r][c] > 0) {
                    double x = c * CELL_SIZE;
                    double y = r * CELL_SIZE;
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.strokeRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }



}
