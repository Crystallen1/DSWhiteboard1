package com.example.whiteboardclient.listener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatListener extends Remote, Serializable {
    void sendMessageToEveryone(String username,String message)throws RemoteException;
}
