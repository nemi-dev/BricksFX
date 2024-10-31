package dev.nemi.bricksfx.model;

import dev.nemi.bricksfx.Bresenham;
import dev.nemi.bricksfx.Pair;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Bricks {
  public static final int CELL_SIZE = 5;
  public static final int COUNT_ROW_DIRECTION = 800 / CELL_SIZE;
  public static final int COUNT_COLUMN_DIRECTION = COUNT_ROW_DIRECTION / 2;

  private int[][] matrix;
  private LinkedList<Ball> balls;
  private long last = 0L;

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
    for (int i = 0; i < 3; i++) {
      double angle = random.nextDouble() * Math.PI * 2;
      addBall(400f, 400f, (float) (vel * Math.cos(angle)), (float) (vel * Math.sin(angle)));
    }

  }

  void addBall(float x, float y, float dx, float dy) {
    balls.add(new Ball(x, y, dx, dy));
  }

  public void update(long now) {
    // collision test
    for (Ball ball : balls) {
      ArrayList<Pair> map = Bresenham.wouldHit(ball.x, ball.y, ball.dx, ball.dy, matrix);
      if (!map.isEmpty()) {
        var head = map.getFirst();
        ball.requestReflectY(head.y() * CELL_SIZE);
        matrix[head.y()][head.x()] = 0;
      }
    }
    for (Ball ball : balls) {
      ball.update(now, last);
    }

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
//    g.setStroke(Color.WHITE);
    for (Ball ball : balls) {
      g.setStroke(Color.WHITE);
      g.setLineWidth(1.5);
      g.strokeLine(ball.x, ball.y, ball.x - ball.dx, ball.y - ball.dy);
      g.setLineWidth(3);
      g.strokeLine(ball.x, ball.y, ball.x - ball.dx / 2, ball.y - ball.dy / 2);
      g.setStroke(Color.RED);
      g.setLineWidth(1.5);
      g.strokeLine(ball.x, ball.y, ball.x + ball.dx, ball.y + ball.dy);
//      g.setLineWidth(0.5);
//      g.strokeLine(ball.x, ball.y, ball.x - ball.dx * 9, ball.y -ball.dy * 9);
//      g.setLineWidth(1.0);
//      g.strokeLine(ball.x, ball.y, ball.x - ball.dx * 4.5, ball.y -ball.dy * 4.5);
//      g.setLineWidth(1.5);
//      g.strokeLine(ball.x, ball.y, ball.x - ball.dx * 3, ball.y -ball.dy * 3);
//      g.setLineWidth(2.0);
//      g.strokeLine(ball.x, ball.y, ball.x - ball.dx * 2.25, ball.y -ball.dy * 2.25);
//      g.setLineWidth(3.0);
//      g.strokeLine(ball.x, ball.y, ball.x - ball.dx, ball.y -ball.dy);
    }
  }



}
