package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.listener.IUserlistListener;
import com.example.whiteboardclient.listener.UserlistListener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserlist extends Remote, Serializable {
    void addUserlistListener(IUserlistListener listener) throws RemoteException;
    void createAdmin(User user) throws RemoteException;
    void createUser(User user) throws RemoteException;
    void kickUser(String username)throws RemoteException;
    void joinUser(String username)throws RemoteException;
}
