package dev.nemi.bricksfx.model;

public class Ball {
  float x, y, dx, dy;
  int[][] trails;

  private Float reflectXAt = null;
  private Float reflectYAt = null;
  public Ball(float x, float y, float dx, float dy) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.trails = new int[16][2];
  }

  public void update(long now, long before) {
    float px = x + dx, py = y + dy;

    float fx = px, fy = py;
    if (px < 0) {
      fx = getReflect(px, 0);
      dx = -dx;
    }
    if (px > 800) {
      fx = getReflect(px, 800);
      dx = -dx;
    }
    if (py < 0) {
      fy = getReflect(py, 0);
      dy = -dy;
    }
    if (py > 800) {
      fy = getReflect(py, 800);
      dy = -dy;
    }
    if (reflectXAt != null) {
      fx = getReflect(px, reflectXAt);
      dx = -dx;
      reflectXAt = null;
    }
    if (reflectYAt != null) {
      fy = getReflect(py, reflectYAt);
      dy = -dy;
      reflectYAt = null;
    }

    x = fx;
    y = fy;
//    reflectXAt = null;
//    reflectYAt = null;
  }

  float getReflect(float predict, float onto) {
    return onto * 2 - predict;
  }

  public void requestReflectX(float k) {
    if (reflectXAt == null) reflectXAt = k;
  }

  public void requestReflectY(float k) {
    if (reflectYAt == null) reflectYAt = k;
  }

  void rebounce(float r) {
    final float vel = (float) Math.sqrt(dx * dx + dy * dy);
    y = 750;
    dx = (float) (vel * Math.cos(r));
    dy = (float) (vel * Math.sin(r));
  }

}
