package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.IUserlistListener;
import com.example.whiteboardclient.listener.UserlistListener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IUserlist extends Remote, Serializable {
    void addUserlistListener(IUserlistListener listener) throws RemoteException;
    void createAdmin(User user) throws RemoteException;
    void createUser(User user) throws RemoteException;
    void kickUser(String username)throws RemoteException;
    void joinUser(String username)throws RemoteException;
    UserManager getUserManager() throws RemoteException;
    void sendMessage(String message) throws RemoteException;
    String receiveMessage() throws RemoteException;
    boolean isUserExists(String username)throws RemoteException;

    void setApprove()throws RemoteException;

}
