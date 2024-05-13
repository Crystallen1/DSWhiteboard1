package com.example.whiteboardclient.listener;


import com.example.whiteboardclient.UserlistUIUpdater;
import com.example.whiteboardclient.WhiteboardUIUpdater;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.io.StringReader;
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
    public void sameUsername(String username) throws RemoteException {
        userlistUIUpdater.warnSameUsername(username);
    }

    @Override
    public void userDisconnected() throws RemoteException {
        if (userlistUIUpdater != null) {
            userlistUIUpdater.updateUserListDisconnected(username);
        }
        // 可以在这里添加更多的逻辑，比如日志记录、发送警告等
        System.out.println(username + " has disconnected.");
    }

    @Override
    public String getListenerName() throws RemoteException {
        return username;
    }

    @Override
    public void kick() throws RemoteException {
        userlistUIUpdater.shutdown();
    }
}
