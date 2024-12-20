package dev.nemi.bricksfx;

import dev.nemi.bricksfx.model.Bricks;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BricksControl {

  @FXML
  private Canvas canvas;
  private AnimationTimer timer;

  GraphicsContext g;
  Bricks bricks;

  @FXML
  public void initialize() {
    g = canvas.getGraphicsContext2D();
    bricks = new Bricks();
    canvas.setFocusTraversable(true);

//    timer = new Loop(g, bricks);
//    timer.start();

    handleOnce();
  }


  @FXML
  void handleKeyPress(KeyEvent ev) {
    if (ev.getCode() == KeyCode.ENTER)
      handleOnce();
  }

  void handleOnce() {
    bricks.update(0);
    g.clearRect(0, 0, 800, 800);
    bricks.render(g);
  }



}
