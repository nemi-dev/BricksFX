package dev.nemi.bricksfx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.nemi.bricksfx.model.Bricks.CELL_SIZE;


public class Bresenham {

  @NotNull
  public static ArrayList<IntXY> would2hit(double x, double y, double dx, double dy, int[][] matrix) {
    ArrayList<IntXY> cellsHit = new ArrayList<>();

    // Scale starting point and direction to cell grid
    x /= CELL_SIZE;
    y /= CELL_SIZE;
    dx /= CELL_SIZE;
    dy /= CELL_SIZE;

    boolean steep = Math.abs(dy) > Math.abs(dx);
    if (steep) {
      // Swap dx and dy for steep lines
      double temp = dx;
      dx = dy;
      dy = temp;
    }

    int steps = (int) Math.round(Math.abs(dx));
    double xStep = dx / steps;
    double yStep = dy / steps;

    double error = Math.abs(dx / 2.0);
    int yDirection = (dy > 0) ? 1 : -1;

    int integerX = (int) Math.round(x);
    int integerY = (int) Math.round(y);

    for (int i = 0; i <= steps; i++) {
      // Check if scaled cell is within matrix bounds and has a non-zero value
      if (integerX >= 0 && integerX < matrix[0].length && integerY >= 0 && integerY < matrix.length) {
        if (matrix[integerY][integerX] != 0) {
          IntXY cell = steep ? new IntXY(integerY, integerX) : new IntXY(integerX, integerY);
          cellsHit.add(cell); // Record only non-zero cells
        }
      }

      // Update x, y, and error
      x += xStep;
      y += yStep;
      integerX = (int) Math.round(x);

      error -= Math.abs(dy);
      if (error < 0) {
        integerY += yDirection;
        error += Math.abs(dx);
      }
    }

    return cellsHit;
  }


  @NotNull
  public static ArrayList<IntXY> wouldHit(double x, double y, double dx, double dy, int[][] grid) {

    ArrayList<IntXY> crossedCells = new ArrayList<>();

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
        try {
          if (grid[startY][startX] != 0) crossedCells.add(new IntXY(startX, startY));
        } catch (ArrayIndexOutOfBoundsException _) {}
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
        try {
          if (grid[startY][startX] != 0) crossedCells.add(new IntXY(startX, startY));
        } catch (ArrayIndexOutOfBoundsException _) {}
        error -= deltaX;
        if (error < 0) {
          startX += stepX;
          error += deltaY;
        }
        startY += stepY;
      }
    }

    // Final check at the endpoint
    try {
      if (grid[endY][endX] != 0) crossedCells.add(new IntXY(endX, endY));
    } catch (ArrayIndexOutOfBoundsException _) {}

    return crossedCells;
  }

}
