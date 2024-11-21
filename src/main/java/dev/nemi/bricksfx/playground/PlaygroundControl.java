package dev.nemi.bricksfx.playground;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PlaygroundControl {

  @FXML
  private Canvas canvas;
  private GraphicsContext gc;

  @FXML
  private Spinner<Integer> sizeSpinner;

  private int count = 5;
  private int[][] matrix = new int[count][count];

  private int fillCellMode = 0;

  private Double startX = null;
  private Double startY = null;
  private Double endX = null;
  private Double endY = null;

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
    gc = canvas.getGraphicsContext2D();
    sizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      setCount(newValue);
    });
    paint();
  }

  public void paint() {
    double fraction = 800.0 / count;

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.setFill(Color.rgb(255, 255, 255, 0.25));
    gc.setStroke(Color.rgb(255, 255, 255, 0.75));
    gc.setLineWidth(0.5);
    for (int i = 0; i < count; i++) {
      for (int j = 0; j < count; j++) {
        if (matrix[i][j] > 0) {
          gc.fillRect(fraction * i, fraction * j, fraction, fraction);
        }
        gc.strokeRect(fraction * i, fraction * j, fraction, fraction);

      }
    }
    if (startX != null && startY != null && endX != null && endY != null) {
      gc.setLineWidth(1.0);
      gc.setStroke(Color.rgb(0, 192, 255, 1.0));
      gc.strokeLine(startX, startY, endX, endY);
    }
  }

  public void setCount(int newValue) {
    count = newValue;
    matrix = new int[count][count];
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
      }
      case SECONDARY -> {
        int c = quantize(event.getX());
        int r = quantize(event.getY());
        fillCellMode = matrix[c][r] > 0? 0 : 1;
        matrix[c][r] = fillCellMode;
      }
    }

    paint();
  }

  public void onCanvasMouseDrag(MouseEvent event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        endX = event.getX();
        endY = event.getY();
      }
      case SECONDARY -> {
        int c = quantize(event.getX());
        int r = quantize(event.getY());
        matrix[c][r] = fillCellMode;
      }
    }
    paint();
  }
}
