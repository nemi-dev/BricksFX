package dev.nemi.bricksfx.playground;

import dev.nemi.bricksfx.BricksApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Playground extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(BricksApp.class.getResource("playground-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("BricksFX");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
