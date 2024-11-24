package dev.nemi.bricksfx;

import java.util.Objects;

public final class Line {
  public double startX;
  public double startY;
  public double endX;
  public double endY;

  public Line(double startX, double startY, double endX, double endY) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
  }

  public void set(double startX, double startY, double endX, double endY) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
  }

  public Line foldX(double onX) {
    if (startX == endX) return this;
    double r = (onX - startX) / (endX - startX);
    double onY = startY + r * (endY - startY);
    return new Line(onX, onY, 2 * onX - endX, endY);
  }

  public Line foldY(double onY) {
    if (startY == endY) return this;
    double r = (onY - startY) / (endY - startY);
    double onX = startX + r * (endX - startX);
    return new Line(onX, onY, endX, 2 * onY - endY);
  }

  public double length() {
    return Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Line) obj;
    return Double.doubleToLongBits(this.startX) == Double.doubleToLongBits(that.startX) &&
      Double.doubleToLongBits(this.startY) == Double.doubleToLongBits(that.startY) &&
      Double.doubleToLongBits(this.endX) == Double.doubleToLongBits(that.endX) &&
      Double.doubleToLongBits(this.endY) == Double.doubleToLongBits(that.endY);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startX, startY, endX, endY);
  }

  @Override
  public String toString() {
    return "Line[" +
      "startX=" + startX + ", " +
      "startY=" + startY + ", " +
      "endX=" + endX + ", " +
      "endY=" + endY + ']';
  }

}
