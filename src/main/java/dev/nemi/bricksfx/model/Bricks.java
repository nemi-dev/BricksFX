package dev.nemi.bricksfx.model;

import dev.nemi.bricksfx.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class Bricks {
  public static final int COL_COUNT = 10;
  public static final int ROW_COUNT = COL_COUNT / 2;

  public static final double CELL_SIZE = 800.0 / COL_COUNT;
//  public static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;

  private int[][] matrix;
  private LinkedList<Ball> balls;
  private long last = 0L;

  private Vector<IntXY> brickBreakRequests = new Vector<>();

  public Bricks() {
    init();
  }

  public void init() {
    matrix = new int[ROW_COUNT][COL_COUNT];
    balls = new LinkedList<>();

    for (int i = 0; i < ROW_COUNT; i++) {
      for (int j = 0; j < COL_COUNT; j++) {
        matrix[i][j] = 1;
      }
    }

    Random random = new Random();
    double vel = 92.0;
    for (int i = 0; i < 3; i++) {
      double angle = Math.PI / 14 * (i + 1) + random.nextDouble(0.75);
//      double angle = Math.PI / 6;
      addBall(400, 600, (vel * Math.cos(angle)), (vel * Math.sin(angle)));
    }

  }

  void addBall(double x, double y, double dx, double dy) {
    balls.add(new Ball(x, y, dx, dy));
  }

  public void update(long now) {
    // resolve ball hit predict
    for (Ball ball : balls) {
      Line current = new Line(ball.x, ball.y,ball.x + ball.dx, ball.y + ball.dy);
      while (true) {
        List<HitInfo> domains = Griding.getDomains(current, CELL_SIZE);
        Line next = null;
        for (HitInfo hitInfo : domains) {
          Integer r = hitInfo.on(matrix);
          if (r != null && r > 0) {
            switch (hitInfo.side()) {
              case HitInfo.TOP -> {
                next = current.foldY(CELL_SIZE * hitInfo.y());
                ball.flipDY();
              }
              case HitInfo.RIGHT -> {
                next = current.foldX(CELL_SIZE * (hitInfo.x() + 1));
                ball.flipDX();
              }
              case HitInfo.BOTTOM -> {
                next = current.foldY(CELL_SIZE * (hitInfo.y() + 1));
                ball.flipDX();
              }
              case HitInfo.LEFT -> {
                next = current.foldX(CELL_SIZE * hitInfo.x());
                ball.flipDY();
              }
              default -> current = null;
            }
            brickBreakRequests.add(hitInfo.intXY());
            break;
          }
          // no reflection upon active block found
          if (current.endX < 0) {
            next = current.foldX(0);
            ball.dx = Math.abs(ball.dx);
          }
          else if (current.endX > 800) {
            next = current.foldX(800);
//            ball.flipDX();
            ball.dx = -Math.abs(ball.dx);
          }
          else if (current.endY < 0) {
            next = current.foldY(0);
//            ball.flipDY();
            ball.dy = Math.abs(ball.dy);
          }
          else if (current.endY > 800) {
            next = current.foldY(800);
//            ball.flipDY();
            ball.dy = -Math.abs(ball.dy);
          }
        }
        if (next == null) break;
        ball.addBreakpoint(next.startX, next.startY);
        ball.pushPosition(next.endX, next.endY);
        System.out.println(next.length());

        current = next;
      }
    }
    for (Ball ball : balls) {
      ball.update(now, last);
    }
    for (var r : brickBreakRequests) {
      try {
        matrix[r.y()][r.x()] = 0;
      } catch (ArrayIndexOutOfBoundsException e) {
      }
    }
    brickBreakRequests.clear();

    last = now;

  }

  public void render(GraphicsContext g) {
    g.setFill(Color.rgb(0, 128, 255, 1.0));
    g.setStroke(Color.rgb(128, 224, 255, 1.0));
    g.setLineWidth(0.5);
    for (int r = 0; r < ROW_COUNT; r += 1) {
      for (int c = 0; c < COL_COUNT; c += 1) {
        if (matrix[r][c] > 0) {
          double x = c * CELL_SIZE;
          double y = r * CELL_SIZE;
          g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
          g.strokeLine(x, y, x + CELL_SIZE, y);
          g.strokeLine(x, y, x, y + CELL_SIZE);
        }
      }
    }
    g.setFill(Color.WHITE);
    for (Ball ball : balls) {
      double cx = ball.x, cy = ball.y;

      g.setStroke(Color.WHITE);
      g.setLineWidth(2.0);

      renderTrail(g, cx, cy, ball.breakpoints, 0, 15.0, 3.0);

      g.setStroke(Color.RED);
      g.setLineWidth(1.5);
      g.strokeLine(ball.x, ball.y, ball.x + ball.dx, ball.y + ball.dy);
    }
  }

  private void renderTrail(GraphicsContext g, double cx, double cy, ArrayList<Point> breakpoints, int index, double remainTrail, double width) {
    double memoryTrail = remainTrail;
    g.setLineWidth(width);
    for (; index < breakpoints.size(); index += 1) {
      Point p = breakpoints.get(index);
      double dx = cx - p.x(), dy = cy - p.y();
      double dis = Math.sqrt(dx * dx + dy * dy);
      if (remainTrail > dis) {
        g.strokeLine(cx, cy, p.x(), p.y());
        cx = p.x();
        cy = p.y();
        remainTrail -= dis;
      } else {
        double r = remainTrail / dis;
        double q = 1 - r;
        double nextX = r * p.x() + q * cx;
        double nextY = r * p.y() + q * cy;
        g.strokeLine(cx, cy, nextX, nextY);
        if (width > 0.5) renderTrail(g, nextX, nextY, breakpoints, index, memoryTrail * 1.5, width - 0.5);
        else {
          int ahead = index + 1;
          while (ahead < breakpoints.size()) breakpoints.remove(ahead);
        }
        break;
      }
    }
  }




}
