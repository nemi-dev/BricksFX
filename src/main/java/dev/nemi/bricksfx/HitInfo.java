package dev.nemi.bricksfx;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @param x
 * @param y
 * @param side
 * <ul>
 *   <li>0 == top</li>
 *   <li>1 == right</li>
 *   <li>2 == bottom</li>
 *   <li>3 == left</li>
 *   <li>-1 == inside</li>
 * </ul>
 */
public record HitInfo (int x, int y, int side) {
  public static final int TOP = 0;
  public static final int RIGHT = 1;
  public static final int BOTTOM = 2;
  public static final int LEFT = 3;
  public static final int INSIDE = -1;
  @Contract(pure = true)
  public Integer on(int[][] matrix) {
    try {
      return matrix[y][x];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  public IntXY intXY() {
    return new IntXY(x, y);
  }
}
