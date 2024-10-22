package dev.nemi.bricksfx;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Bricks {
    public static final int CELL_SIZE = 5;
    private static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;
    private static final int COUNT_COLUMN_DIRECTION = COUNT_ROW_DIRECTION / 2;

    private class Ball {
        float x, y, dx, dy;
        int[][] trails;
        public Ball(float x, float y, float dx, float dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.trails = new int[16][2];
        }

        public void update(long now, long before) {

        }

        void willHit() {
            float nextX = x + dx;
            float nextY = y + dy;
        }

        public ArrayList<Pair<Integer, Integer>> getCrossedCells() {
            ArrayList<Pair<Integer, Integer>> crossedCells = new ArrayList<>();

            // Calculate the starting and ending positions
            int startX = (int) Math.floor(x / CELL_SIZE);
            int startY = (int) Math.floor(y / CELL_SIZE);
            int endX = (int) Math.floor((x + dx) / CELL_SIZE);
            int endY = (int) Math.floor((y + dy) / CELL_SIZE);

            // Use Bresenham's line algorithm
            int deltaX = endX - startX;
            int deltaY = endY - startY;

            int stepX = Integer.compare(deltaX, 0);
            int stepY = Integer.compare(deltaY, 0);

            // Calculate the absolute values of deltaX and deltaY
            deltaX = Math.abs(deltaX);
            deltaY = Math.abs(deltaY);

            // Handle the case when moving more in the x direction
            if (deltaX > deltaY) {
                int error = deltaX / 2;
                while (startX != endX) {
                    // Add to crossed cells if the point has crossed into the interior
                    if (x > (startX * CELL_SIZE) && x < ((startX + 1) * CELL_SIZE) &&
                            y > (startY * CELL_SIZE) && y < ((startY + 1) * CELL_SIZE)) {
                        crossedCells.add(new Pair<>(startX, startY)); // Store the cell
                    }
                    error -= deltaY;
                    if (error < 0) {
                        startY += stepY;
                        error += deltaX;
                    }
                    startX += stepX;
                }
            } else {
                int error = deltaY / 2;
                while (startY != endY) {
                    // Add to crossed cells if the point has crossed into the interior
                    if (x > (startX * CELL_SIZE) && x < ((startX + 1) * CELL_SIZE) &&
                            y > (startY * CELL_SIZE) && y < ((startY + 1) * CELL_SIZE)) {
                        crossedCells.add(new Pair<>(startX, startY)); // Store the cell
                    }
                    error -= deltaX;
                    if (error < 0) {
                        startX += stepX;
                        error += deltaY;
                    }
                    startY += stepY;
                }
            }

            // Final check at the endpoint
            if (x > (endX * CELL_SIZE) && x < ((endX + 1) * CELL_SIZE) &&
                    y > (endY * CELL_SIZE) && y < ((endY + 1) * CELL_SIZE)) {
                crossedCells.add(new Pair<>(endX, endY));
            }

            return crossedCells;
        }
        void rebounce(float r) {

        }

    }

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
