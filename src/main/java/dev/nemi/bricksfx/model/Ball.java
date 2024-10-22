package dev.nemi.bricksfx.model;

public class Ball {
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
        x += dx;
        y += dy;
    }

    void rebounce(float r) {
        final float vel = (float) Math.sqrt(dx * dx + dy * dy);
    }

}
