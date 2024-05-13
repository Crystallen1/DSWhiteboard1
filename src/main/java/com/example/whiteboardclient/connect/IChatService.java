package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.listener.IChatListener;
import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatService extends Remote {
    void sendMessage(String username,String message) throws RemoteException;
    void addChatListener(IChatListener listener) throws RemoteException;

}
