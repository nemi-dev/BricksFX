package dev.nemi.bricksfx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dev.nemi.bricksfx.model.Bricks.CELL_SIZE;


public class Bresenham {
  @NotNull
  public static ArrayList<Pair> wouldHit(float x, float y, float dx, float dy, int[][] grid) {

    ArrayList<Pair> crossedCells = new ArrayList<>();

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
          if (grid[startY][startX] != 0) crossedCells.add(new Pair(startX, startY));
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
          if (grid[startY][startX] != 0) crossedCells.add(new Pair(startX, startY));
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
      if (grid[endY][endX] != 0) crossedCells.add(new Pair(endX, endY));
    } catch (ArrayIndexOutOfBoundsException _) {}

    return crossedCells;
  }

}
