package com.example.whiteboardclient.listener;


import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserlistListener extends Remote, Serializable {
    void updateUserManager(UserManager userManager)throws RemoteException;
    void joinRequest(String username)throws RemoteException;
    boolean getAdmin()throws RemoteException;
    void sameUsername(String username)throws RemoteException;
    void userDisconnected()throws RemoteException;
    String getListenerName()throws RemoteException;
    void kick(String message)throws RemoteException;

}
