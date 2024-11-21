package dev.nemi.bricksfx;

import org.jetbrains.annotations.Contract;

public record MatrixCoord(int row, int col) {
  @Contract(pure = true)
  public Integer on(int[][] matrix) {
    try {
      return matrix[row][col];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
}
