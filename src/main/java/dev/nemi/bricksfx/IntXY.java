package dev.nemi.bricksfx;

import org.jetbrains.annotations.Contract;

public record IntXY(int x, int y) {
  @Contract(pure = true)
  public Integer on(int[][] matrix) {
    try {
      return matrix[y][x];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
}
