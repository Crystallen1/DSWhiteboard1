package com.example.whiteboardclient.listener;


import com.example.whiteboardclient.UIUpdater.UserlistUIUpdater;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserlistListener extends UnicastRemoteObject implements IUserlistListener, Serializable {
    private UserlistUIUpdater userlistUIUpdater;
    private boolean isAdmin;
    private String username;

    public UserlistListener(UserlistUIUpdater userlistUIUpdater, boolean isAdmin,String username) throws RemoteException {
        super();
        this.userlistUIUpdater = userlistUIUpdater;
        this.isAdmin = isAdmin;
        this.username=username;
    }


    @Override
    public void updateUserManager(UserManager userManager) throws RemoteException {
        userlistUIUpdater.addUserToList(userManager);

    }

    @Override
    public void joinRequest(String username) throws RemoteException {
        userlistUIUpdater.showRequestDialog(username);
    }

    @Override
    public boolean getAdmin() throws RemoteException {
        return isAdmin;
    }

    @Override
    public String getListenerName() throws RemoteException {
        return username;
    }

    @Override
    public void kick(String message) throws RemoteException {
        userlistUIUpdater.shutdown(message);
    }
}
