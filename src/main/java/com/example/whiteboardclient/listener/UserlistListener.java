package com.example.whiteboardclient.listener;


import com.example.whiteboardclient.UserlistUIUpdater;
import com.example.whiteboardclient.WhiteboardUIUpdater;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserlistListener extends UnicastRemoteObject implements IUserlistListener, Serializable {
    private UserlistUIUpdater userlistUIUpdater;
    private boolean isAdmin;

    public UserlistListener(UserlistUIUpdater userlistUIUpdater, boolean isAdmin) throws RemoteException {
        super();
        this.userlistUIUpdater = userlistUIUpdater;
        this.isAdmin = isAdmin;
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
}
