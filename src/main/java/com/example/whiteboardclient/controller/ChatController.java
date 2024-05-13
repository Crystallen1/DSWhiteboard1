package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.ChatUIUpdater;
import com.example.whiteboardclient.UserlistUIUpdater;
import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.listener.ChatListener;
import com.example.whiteboardclient.listener.WhiteboardListener;
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
    private IChatService server; // RMI 服务接口

    public void initialize() {
        try {
            String remoteObjectName = "//localhost:20017/ChatServer";
            // RMI 服务器查找
            server = (IChatService) Naming.lookup(remoteObjectName);
            ChatListener listener = new ChatListener(this);
            server.addChatListener(listener);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
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
