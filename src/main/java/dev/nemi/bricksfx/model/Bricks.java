package dev.nemi.bricksfx.model;

import java.util.LinkedList;

public class Bricks {
    public static final int CELL_SIZE = 5;
    private static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;
    private static final int COUNT_COLUMN_DIRECTION = COUNT_ROW_DIRECTION / 2;

    private int[][] matrice;
    private LinkedList<Ball> balls;
    private long last = 0L;

    public Bricks() {
        init();
    }

    public void init() {
        matrice = new int[COUNT_COLUMN_DIRECTION][COUNT_ROW_DIRECTION];
        balls = new LinkedList<>();

        for (int i = 0; i < COUNT_COLUMN_DIRECTION; i++) {
            for (int j = 0; j < COUNT_ROW_DIRECTION; j++) {
                matrice[i][j] = 1;
            }
        }
    }

    public void update(long now) {

        for (Ball ball : balls) {
            ball.update(now, last);
        }

        last = now;
    }


    void addBall(float x, float y, float dx, float dy) {
        balls.add(new Ball(x, y, dx, dy));
    }


}
