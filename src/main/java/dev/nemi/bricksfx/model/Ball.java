package dev.nemi.bricksfx.model;

import dev.nemi.bricksfx.Point;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Ball {
  double x;
  double y;
  double dx;
  double dy;
  public final ArrayList<Point> breakpoints = new ArrayList<>();

  private Double reflectXAt = null;
  private Double reflectYAt = null;
  public Ball(double x, double y, double dx, double dy) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    breakpoints.addLast(new Point(x, y));
  }

  public void update(long now, long before) {
    double px = x + dx, py = y + dy;

    double fx = px, fy = py;
    if (px < 0) {
      fx = getReflect(px, 0);
      double rx = 0;
      double yCut = y + (rx - x) * dy / dx;
      Point point = new Point(rx, yCut);
      breakpoints.addFirst(point);
      dx = -dx;
    }
    if (px > 800) {
      fx = getReflect(px, 800);
      double rx = 800;
      double yCut = y + (rx - x) * dy / dx;
      Point point = new Point(rx, yCut);
      breakpoints.addFirst(point);
      dx = -dx;
    }
    if (py < 0) {
      fy = getReflect(py, 0);
      double ry = 0;
      double xCut = x + (ry - y) * dx / dy;
      Point point = new Point(xCut, ry);
      breakpoints.addFirst(point);
      dy = -dy;
    }
    if (py > 800) {
      fy = getReflect(py, 800);
      double ry = 800;
      double xCut = x + (ry - y) * dx / dy;
      Point point = new Point(xCut, ry);
      breakpoints.addFirst(point);
      dy = -dy;
    }
    if (reflectXAt != null) {
      fx = getReflect(px, reflectXAt);
      double yCut = y + (reflectXAt - x) * dy / dx;
      Point point = new Point(reflectXAt, yCut);
      breakpoints.addFirst(point);
      dx = -dx;
      reflectXAt = null;
    }
    if (reflectYAt != null) {
      fy = getReflect(py, reflectYAt);
      double xCut = x + (reflectYAt - y) * dx / dy;
      Point point = new Point(xCut, reflectYAt);
      breakpoints.addFirst(point);
      dy = -dy;
      reflectYAt = null;
    }

    x = fx;
    y = fy;
  }

  double getReflect(double predict, double onto) {
    return onto * 2 - predict;
  }

  public void requestReflectX(double k) {
    if (reflectXAt == null) reflectXAt = k;
  }

  public void requestReflectY(double k) {
    if (reflectYAt == null) reflectYAt = k;
  }

  void rebounce(double r) {
    final double vel = Math.sqrt(dx * dx + dy * dy);
    y = 750;
    dx = vel * Math.cos(r);
    dy = vel * Math.sin(r);
  }

}
