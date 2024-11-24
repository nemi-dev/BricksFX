package dev.nemi.bricksfx.playground;


import dev.nemi.bricksfx.Griding;
import dev.nemi.bricksfx.HitInfo;
import dev.nemi.bricksfx.IntXY;
import dev.nemi.bricksfx.Line;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PlaygroundControl {

  @FXML
  private Canvas canvas;
  private GraphicsContext g;

  @FXML
  private Spinner<Integer> sizeSpinner;


  private Line line = null;
//  private Double startX = null;
//  private Double startY = null;
//  private Double endX = null;
//  private Double endY = null;

  private int count = 25;
  private double cellSize = 800.0 / count;
  private int[][] matrix = new int[count][count];

  private int fillCellMode = 0;
  private int startC = -1;
  private int startR = -1;
  private int endR = -1;
  private int endC = -1;
  private boolean drawingSquare = false;

  private List<HitInfo> domains = null;

  private void fillMat() {
    for (int i = 0; i < count; i++) {
      for (int j = 0; j < count; j++) {
        matrix[i][j] = 1;
      }
    }
  }

  private int quantize(double v) {
    return (int) Math.floor(v / 800 * count);
  }

  @FXML
  public void initialize() {
    fillMat();
    g = canvas.getGraphicsContext2D();
    sizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      setCount(newValue);
    });
    paint();
  }

  public void setCount(int newValue) {
    count = newValue;
    matrix = new int[count][count];
    cellSize = 800.0 / count;
//    if (startX != null && startY != null && endX != null && endY != null)
    if (line != null)
      domains = Griding.getDomains(line, cellSize);
    fillMat();
    paint();
  }

  public void onCanvasMouseDown(MouseEvent event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        line = new Line(event.getX(), event.getY(), event.getX(), event.getY());
//        line.startX = event.getX();
//        line.startY = event.getY();
//        line.endX = event.getX();
//        line.endY = event.getY();
        domains = Griding.getDomains(line, cellSize);
      }
      case SECONDARY -> {
        int c = quantize(event.getX());
        int r = quantize(event.getY());
        if (c < 0 || c >= count || r < 0 || r >= count) break;
        fillCellMode = matrix[r][c] > 0? 0 : 1;
        matrix[r][c] = fillCellMode;
        startR = endR = r;
        startC = endC = c;
        drawingSquare = true;
      }
    }

    paint();
  }

  public void onCanvasMouseDrag(MouseEvent event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        line.endX = event.getX();
        line.endY = event.getY();
        domains = Griding.getDomains(line, cellSize);
      }
      case SECONDARY -> {
        int currentC = quantize(event.getX());
        int currentR = quantize(event.getY());
        if (currentC < 0 || currentC >= count || currentR < 0 || currentR >= count) break;
        endR = currentR;
        endC = currentC;

      }
    }
    paint();
  }

  public void onCanvasMouseUp(@NotNull MouseEvent event) {
    if (Objects.requireNonNull(event.getButton()) == MouseButton.SECONDARY &&
      startR != -1 && startC != -1 && endR != -1 && endC != -1) {
      int currentC = quantize(event.getX());
      int currentR = quantize(event.getY());
      if (currentC < 0 || currentC >= count || currentR < 0 || currentR >= count) return;
      endR = currentR;
      endC = currentC;
      int dirR = (int) Math.signum(endR - startR);
      int dirC = (int) Math.signum(endC - startC);
      for (int r = startR; r * dirR <= currentR * dirR; r += dirR) {
        for (int c = startC; c * dirC <= currentC * dirC; c += dirC) {
          matrix[r][c] = fillCellMode;
          if (dirC == 0) break;
        }
        if (dirR == 0) break;
      }
      startR = -1;
      startC = -1;
      endR = -1;
      endC = -1;
      drawingSquare = false;
      paint();
    }
  }



  public void paint() {

    g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    g.setFill(Color.rgb(255, 255, 255, 0.25));
    g.setStroke(Color.rgb(255, 255, 255, 0.75));
    g.setLineWidth(0.5);
    for (int r = 0; r < count; r++) {
      for (int c = 0; c < count; c++) {
        int matrixValue;
        if (Griding.between(startR, r, endR) && Griding.between(startC, c, endC)) {
          matrixValue = fillCellMode;
        } else {
          matrixValue = matrix[r][c];
        }
        if (matrixValue > 0) {
          g.fillRect(cellSize * c, cellSize * r, cellSize, cellSize);
        }
        g.strokeRect(cellSize * c, cellSize * r, cellSize, cellSize);

      }
    }
    if (domains != null) {
      int index = 0;
      for (HitInfo hitInfo : domains) {
        g.setFill(Color.rgb(255, 117, 224, 0.25));
        g.fillRect(cellSize * hitInfo.x(), cellSize * hitInfo.y(), cellSize, cellSize);
        Integer on = hitInfo.on(matrix);
        if (on != null && on > 0) {
          g.setFill(Color.WHITE);
          g.fillText(String.valueOf(index), cellSize * (hitInfo.x() + 0.5), cellSize * (hitInfo.y() + 0.5));
          index += 1;
        }
        g.setFill(Color.rgb(204, 128,255, 0.5));
        switch (hitInfo.side()) {
          case HitInfo.TOP -> g.fillRect(cellSize * hitInfo.x(), cellSize * hitInfo.y(), cellSize, 5);
          case HitInfo.RIGHT -> g.fillRect(cellSize * (hitInfo.x() + 1) - 5, cellSize * hitInfo.y(), 5, cellSize);
          case HitInfo.BOTTOM -> g.fillRect(cellSize * hitInfo.x(), cellSize * (hitInfo.y() + 1) - 5, cellSize, 5);
          case HitInfo.LEFT -> g.fillRect(cellSize * hitInfo.x(), cellSize * hitInfo.y(), 5, cellSize);
          case HitInfo.INSIDE -> g.fillOval(cellSize * (hitInfo.x() + 0.5) - 2.5, cellSize * (hitInfo.y() + 0.5) - 2.5, 5, 5);
        }
      }
    }
    if (line != null) {
      g.setLineWidth(1.0);
      g.setStroke(Color.rgb(0, 192, 255, 1.0));
      g.strokeLine(line.startX, line.startY, line.endX, line.endY);
    }
  }

}
