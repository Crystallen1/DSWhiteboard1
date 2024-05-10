package com.example.whiteboardclient.connect;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatService extends Remote {
    void sendMessage(String message) throws RemoteException;
    List<String> getChatHistory() throws RemoteException;
}
