module dev.nemi.bricksfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;


    opens dev.nemi.bricksfx to javafx.fxml;
    exports dev.nemi.bricksfx;
}