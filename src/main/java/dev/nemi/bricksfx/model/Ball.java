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
        float px = x + dx, py = y + dy;

        if (px < 0) {
            x = getReflect(px, 0);
            dx = -dx;
        }
        if (px > 800) {
            x = getReflect(px, 800);
            dx = -dx;
        }
        if (py < 0) {
            y = getReflect(py, 0);
            dy = -dy;
        }
        if (py > 800) {
            y = getReflect(py, 800);
            dy = -dy;
        }

        x = px;
        y = py;
    }

    float getReflect(float predict, float onto) {
        return onto * 2 - predict;
    }

    void rebounce(float r) {
        final float vel = (float) Math.sqrt(dx * dx + dy * dy);
    }

}
