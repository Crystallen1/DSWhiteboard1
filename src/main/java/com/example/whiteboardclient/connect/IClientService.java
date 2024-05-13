package com.example.whiteboardclient.connect;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientService extends Remote {
    void disconnect() throws RemoteException;

}
