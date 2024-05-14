package com.example.whiteboardclient.listener;

import com.example.whiteboardclient.UIUpdater.ChatUIUpdater;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatListener extends UnicastRemoteObject implements IChatListener, Serializable {
    private ChatUIUpdater chatUIUpdater;

    public ChatListener(ChatUIUpdater chatUIUpdater) throws RemoteException {
        super();
        this.chatUIUpdater=chatUIUpdater;
    }

    @Override
    public void sendMessageToEveryone(String username, String message) {
        chatUIUpdater.updateChatBox(username,message);
    }
}
