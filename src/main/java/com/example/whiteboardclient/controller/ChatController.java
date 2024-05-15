package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.UIUpdater.ChatUIUpdater;
import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.listener.ChatListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ChatController  implements Serializable, ChatUIUpdater {
    public TextArea chatArea;
    public TextField chatInput;
    private IChatService server; // RMI interface

    public void initialize() {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/ChatServer";
            // Look up the RMI server using the constructed remote object name.
            server = (IChatService) Naming.lookup(remoteObjectName);
            ChatListener listener = new ChatListener(this);
            server.addChatListener(listener);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
            // Display an error alert dialog to inform the user of the connection failure.
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            // Close the main application window upon connection failure.
            System.exit(1);
        }
    }
    public void sendChat(ActionEvent actionEvent) throws RemoteException {
        server.sendMessage(WhiteBoardApplication.getUsername(),chatInput.getText());
    }

    @Override
    public void updateChatBox(String username, String message) {
        Platform.runLater(() -> {
            chatArea.appendText(username+": "+message+"\n");
        });
    }
}
