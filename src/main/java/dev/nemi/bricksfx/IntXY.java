package dev.nemi.bricksfx;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record IntXY(int x, int y) {
  @Contract(pure = true)
  public Integer on(int[] @NotNull [] matrix) {
    try {
      return matrix[y][x];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
}
