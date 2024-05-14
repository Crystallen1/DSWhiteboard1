module com.example.whiteboardclient {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires shared.rmi.interfaces;
    requires java.rmi;
    //requires javax.websocket.api;

    opens com.example.whiteboardclient to javafx.fxml;
    exports com.example.whiteboardclient;
    exports com.example.whiteboardclient.controller;
    opens com.example.whiteboardclient.controller to javafx.fxml;
    exports com.example.whiteboardclient.listener;
    opens com.example.whiteboardclient.listener to javafx.fxml;
    exports com.example.whiteboardclient.datamodel;
    opens com.example.whiteboardclient.datamodel to javafx.fxml;
    exports com.example.whiteboardclient.connect;
    opens com.example.whiteboardclient.connect to javafx.fxml;
    exports com.example.whiteboardclient.UIUpdater;
    opens com.example.whiteboardclient.UIUpdater to javafx.fxml;


}