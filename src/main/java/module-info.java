module com.example.whiteboardclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    //requires javax.websocket.api;

    opens com.example.whiteboardclient to javafx.fxml;
    exports com.example.whiteboardclient;

}