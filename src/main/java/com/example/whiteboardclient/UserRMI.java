package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.IUserlistListener;
import com.example.whiteboardclient.listener.IWhiteboardListener;
import com.example.whiteboardclient.listener.UserlistListener;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class UserRMI extends UnicastRemoteObject implements IUserlist, Serializable {
    private List<IUserlistListener> listeners = new ArrayList<>();
    private UserManager userManager;

    public UserRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager = userManager;
    }

    protected  void notifyListeners(UserManager userManager)throws RemoteException {
        for (IUserlistListener listener : listeners) {
            listener.updateUserManager(userManager);
        }
    }
    protected  void notifyListeners(String username)throws RemoteException {
        for (IUserlistListener listener : listeners) {
            if (listener.getAdmin()){
                listener.joinRequest(username);
            }
        }
    }
    @Override
    public void addUserlistListener(IUserlistListener listener) throws RemoteException {
        listeners.add(listener);

    }

    @Override
    public void createAdmin(User user) throws RemoteException {

            System.out.println("add new admin:"+user.getUsername());
            userManager.addUser(user);
            userManager.setAdminUser(user);
            notifyListeners(userManager);
    }

    @Override
    public void createUser(User user) throws RemoteException {
        System.out.println("add new user:"+user.getUsername());
        userManager.addUser(user);
        notifyListeners(userManager);
    }

    @Override
    public void kickUser(String username) throws RemoteException{
        System.out.println("kick user:"+username);
        userManager.deleteUser(username);
        notifyListeners(userManager);
    }

    @Override
    public void joinUser(String username) throws RemoteException {
        System.out.println(username+" want to join");
        notifyListeners(username);
    }

}
