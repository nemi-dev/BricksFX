package dev.nemi.bricksfx;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Griding {

  public static boolean between(int start, int center, int end) {
    int d = (int) Math.signum(end - start);
    if (d == 0) return start == center;
    return start * d <= center * d && center * d <= end * d;
  }

  public static double reflection(double in, double k) {
    return 2 * k - in;
  }

  @Contract(pure = true)
  public static int quantize(double v, double cellSize) { return (int) Math.floor(v / cellSize); }

  @Contract(pure = true)
  public static @NotNull List<HitInfo> getDomains(double startX, double startY, double endX, double endY, double cellSize) {
    List<HitInfo> domains = new ArrayList<>();
    final int startCX = (int) (startX / cellSize);
    final int startCY = (int) (startY / cellSize);
    final int endCX = (int) (endX / cellSize);
    final int endCY = (int) (endY / cellSize);
    final int dirX = (int) Math.signum(endCX - startCX);
    final int dirY = (int) Math.signum(endCY - startCY);

    if (startCX == endCX) {
      if (startCY == endCY) {
        // Ends up inside
        domains.add(new HitInfo(startCX, startCY, HitInfo.INSIDE));
        return domains;
      }
      // Vertical Steep
      for (int i = startCY; i * dirY <= endCY * dirY; i += dirY) {
        int side = i == startCY ? HitInfo.INSIDE : dirY == 1 ? HitInfo.TOP : HitInfo.BOTTOM;
        domains.add(new HitInfo(startCX, i, side));
      }
      return domains;
    } else if (startCY == endCY) {
      // Flat Horizontal
      for (int i = startCX; i * dirX <= endCX * dirX; i += dirX) {
        int side = i == startCX ? HitInfo.INSIDE : dirX == 1 ? HitInfo.LEFT : HitInfo.RIGHT;
        domains.add(new HitInfo(i, startCY, side));
      }
      return domains;
    }


    double skew = (endY - startY) / (endX - startX);
    double currentX = startX;
    double currentY = startY;
    int curIX = startCX;
    int curIY = startCY;

    int vside = dirY > 0 ? HitInfo.TOP : HitInfo.BOTTOM;
    int hside = dirX > 0 ? HitInfo.LEFT : HitInfo.RIGHT;
    if (skew > 1 || skew < -1) {
      while (curIX * dirX <= endCX * dirX) {
        double nextX = (curIX + (dirX == 1? 1 : 0)) * cellSize;
        if (nextX * dirX > endX * dirX) nextX = endX;

        if (endX == currentX) {
          domains.add(new HitInfo(curIX, curIY, hside));
          return domains;
        }
        double nextY = currentY + (nextX - currentX) * skew;

        int nextIY = quantize(nextY, cellSize);
        for (int i = curIY; i * dirY <= nextIY * dirY; i += dirY) {
          int side;
          if (curIY == i) {
            side = curIX == startCX ? HitInfo.INSIDE : hside;
          } else {
            side = vside;
          }
          domains.add(new HitInfo(curIX, i, side));
        }

        currentX = nextX;
        currentY = nextY;
        curIX += dirX;
        curIY = nextIY;
      }
    } else {
      while (curIY * dirY <= endCY * dirY) {
        double nextY = (curIY + (dirY == 1? 1 : 0)) * cellSize;
        if (nextY * dirY > endY * dirY) nextY = endY;

        if (endY == currentY) {
          domains.add(new HitInfo(curIX, curIY, vside));
          return domains;
        }

        double nextX = currentX + (nextY - currentY) / skew;
        int nextIX = quantize(nextX, cellSize);
        for (int i = curIX; i * dirX <= nextIX * dirX; i += dirX) {
          int side;
          if (curIX == i) {
            side = curIY == startCX ? HitInfo.INSIDE : vside;
          } else {
            side = hside;
          }
          domains.add(new HitInfo(i, curIY, side));
        }

        currentY = nextY;
        currentX = nextX;
        curIY += dirY;
        curIX = nextIX;
      }
    }
    return domains;
  }

  public static @NotNull List<HitInfo> getDomains(Line line, double cellSize) {
    return getDomains(line.startX, line.startY, line.endX, line.endY, cellSize);
  }

}
