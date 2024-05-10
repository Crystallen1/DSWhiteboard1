package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatRMI extends UnicastRemoteObject implements IChatService, Serializable {
    private UserManager userManager;

    protected ChatRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager = userManager;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {

    }

    @Override
    public List<String> getChatHistory() throws RemoteException {
        return null;
    }
}
