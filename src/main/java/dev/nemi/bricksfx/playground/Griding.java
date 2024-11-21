package dev.nemi.bricksfx.playground;

import dev.nemi.bricksfx.IntXY;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Griding {

  @Contract(pure = true)
  public static int quantize(double v, double cellSize) { return (int) Math.floor(v / cellSize); }

  public static @NotNull List<IntXY> getDomains(double startX, double startY, double endX, double endY, double cellSize) {
    List<IntXY> domains = new ArrayList<>();
    final int startCX = (int) (startX / cellSize);
    final int startCY = (int) (startY / cellSize);
    final int endCX = (int) (endX / cellSize);
    final int endCY = (int) (endY / cellSize);
    final int dirX = (int) Math.signum(endCX - startCX);
    final int dirY = (int) Math.signum(endCY - startCY);

    if (startCX == endCX) {
      if (startCY == endCY) {
        domains.add(new IntXY(startCX, startCY));
        return domains;
      }
      for (int i = startCY; i * dirY <= endCY * dirY; i += dirY) {
        domains.add(new IntXY(startCX, i));
      }
      return domains;
    } else if (startCY == endCY) {
      for (int i = startCX; i * dirX <= endCX * dirX; i += dirX) {
        domains.add(new IntXY(i, startCY));
      }
      return domains;
    }


    double skew = (endY - startY) / (endX - startX);
    double currentX = startX;
    double currentY = startY;
    int curIX = startCX;
    int curIY = startCY;

    if (skew > 1 || skew < -1) {
      while (curIX * dirX <= endCX * dirX) {
        double nextX = (curIX + (dirX == 1? 1 : 0)) * cellSize;
        if (nextX * dirX > endX * dirX) nextX = endX;

        if (endX == currentX) {
          domains.add(new IntXY(curIX, curIY));
          return domains;
        }
        double nextY = currentY + (nextX - currentX) * skew;

        int nextIY = quantize(nextY, cellSize);
        for (int i = curIY; i * dirY <= nextIY * dirY; i += dirY) {
          domains.add(new IntXY(curIX, i));
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
          domains.add(new IntXY(curIX, curIY));
          return domains;
        }

        double nextX = currentX + (nextY - currentY) / skew;
        int nextIX = quantize(nextX, cellSize);
        for (int i = curIX; i * dirX <= nextIX * dirX; i += dirX) {
          domains.add(new IntXY(i, curIY));
        }

        currentY = nextY;
        currentX = nextX;
        curIY += dirY;
        curIX = nextIX;
      }
    }
    return domains;
  }

}
