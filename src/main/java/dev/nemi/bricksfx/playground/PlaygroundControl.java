package dev.nemi.bricksfx.playground;


import dev.nemi.bricksfx.IntXY;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class PlaygroundControl {

  @FXML
  private Canvas canvas;
  private GraphicsContext g;

  @FXML
  private Spinner<Integer> sizeSpinner;



  private Double startX = null;
  private Double startY = null;
  private Double endX = null;
  private Double endY = null;

  private int count = 5;
  private double cellSize = 800.0 / count;
  private int[][] matrix = new int[count][count];

  private int fillCellMode = 0;

  private List<IntXY> domains = null;

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

  public void paint() {

    g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    g.setFill(Color.rgb(255, 255, 255, 0.25));
    g.setStroke(Color.rgb(255, 255, 255, 0.75));
    g.setLineWidth(0.5);
    for (int r = 0; r < count; r++) {
      for (int c = 0; c < count; c++) {
        if (matrix[r][c] > 0) {
          g.fillRect(cellSize * c, cellSize * r, cellSize, cellSize);
        }
        g.strokeRect(cellSize * c, cellSize * r, cellSize, cellSize);

      }
    }
    if (domains != null) {
      int index = 0;
      for (IntXY coord : domains) {
        g.setFill(Color.rgb(255, 117, 224, 0.25));
        g.fillRect(cellSize * coord.x(), cellSize * coord.y(), cellSize, cellSize);
        Integer on = coord.on(matrix);
        if (on != null && on > 0) {
          g.setFill(Color.WHITE);
          g.fillText(String.valueOf(index), cellSize * (coord.x() + 0.5), cellSize * (coord.y() + 0.5));
          index += 1;
        }
      }
    }
    if (startX != null && startY != null && endX != null && endY != null) {
      g.setLineWidth(1.0);
      g.setStroke(Color.rgb(0, 192, 255, 1.0));
      g.strokeLine(startX, startY, endX, endY);
    }
  }

  public void setCount(int newValue) {
    count = newValue;
    matrix = new int[count][count];
    cellSize = 800.0 / count;
    if (startX != null && startY != null && endX != null && endY != null)
      domains = Griding.getDomains(startX, startY, endX, endY, cellSize);
    fillMat();
    paint();
  }

  public void onCanvasMouseDown(MouseEvent event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        startX = event.getX();
        startY = event.getY();
        endX = event.getX();
        endY = event.getY();
        domains = Griding.getDomains(startX, startY, endX, endY, cellSize);
      }
      case SECONDARY -> {
        int c = quantize(event.getX());
        int r = quantize(event.getY());
        fillCellMode = matrix[r][c] > 0? 0 : 1;
        matrix[r][c] = fillCellMode;
      }
    }

    paint();
  }

  public void onCanvasMouseDrag(MouseEvent event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        endX = event.getX();
        endY = event.getY();
        domains = Griding.getDomains(startX, startY, endX, endY, cellSize);
      }
      case SECONDARY -> {
        int c = quantize(event.getX());
        int r = quantize(event.getY());
        matrix[r][c] = fillCellMode;
      }
    }
    paint();
  }
}
