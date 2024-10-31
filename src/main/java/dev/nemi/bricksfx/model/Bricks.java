package dev.nemi.bricksfx.model;

import dev.nemi.bricksfx.Bresenham;
import dev.nemi.bricksfx.IntXY;
import dev.nemi.bricksfx.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class Bricks {
  public static final int CELL_SIZE = 5;
  public static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;
  public static final int COUNT_COLUMN_DIRECTION = COUNT_ROW_DIRECTION / 2;

  private int[][] matrix;
  private LinkedList<Ball> balls;
  private long last = 0L;

  private Vector<IntXY> brickBreakRequests = new Vector<>();

  public Bricks() {
    init();
  }

  public void init() {
    matrix = new int[COUNT_COLUMN_DIRECTION][COUNT_ROW_DIRECTION];
    balls = new LinkedList<>();

    for (int i = 0; i < COUNT_COLUMN_DIRECTION; i++) {
      for (int j = 0; j < COUNT_ROW_DIRECTION; j++) {
        matrix[i][j] = 1;
      }
    }

    Random random = new Random();
    double vel = 18;
    for (int i = 0; i < 9; i++) {
      double angle = random.nextDouble() * Math.PI * 2;
      addBall(400f, 600f, (float) (vel * Math.cos(angle)), (float) (vel * Math.sin(angle)));
    }

  }

  void addBall(float x, float y, float dx, float dy) {
    balls.add(new Ball(x, y, dx, dy));
  }

  public void update(long now) {
    // collision test
    for (Ball ball : balls) {
      ArrayList<IntXY> map = Bresenham.wouldHit(ball.x, ball.y, ball.dx, ball.dy, matrix);
      if (!map.isEmpty()) {
        var head = map.getFirst();
        ball.requestReflectY((head.y() + 1) * CELL_SIZE);
        brickBreakRequests.add(new IntXY(head.x(), head.y()));

//        matrix[head.y()][head.x()] = 0;
      }
    }
    for (Ball ball : balls) {
      ball.update(now, last);
    }
    for (var r : brickBreakRequests) {
      matrix[r.y()][r.x()] = 0;
    }
    brickBreakRequests.clear();

    last = now;

  }

  public void render(GraphicsContext g) {
    g.setFill(Color.rgb(0, 128, 255, 1.0));
    for (int r = 0; r < COUNT_COLUMN_DIRECTION; r += 1) {
      for (int c = 0; c < COUNT_ROW_DIRECTION; c += 1) {
        if (matrix[r][c] > 0) {
          double x = c * CELL_SIZE;
          double y = r * CELL_SIZE;
          g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
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
