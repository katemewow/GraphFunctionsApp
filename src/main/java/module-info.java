module com.example.graphfunctionsapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires com.almasb.fxgl.all;
    requires org.json;

    opens com.example.graphfunctionsapp to javafx.fxml;
    exports com.example.graphfunctionsapp;
}