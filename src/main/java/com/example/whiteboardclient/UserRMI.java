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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserRMI extends UnicastRemoteObject implements IUserlist, Serializable {
    private List<IUserlistListener> listeners = new CopyOnWriteArrayList<>();
    private UserManager userManager;
    private boolean isApprove=false;

    private final BlockingQueue<String> messages = new ArrayBlockingQueue<>(10);
    public UserManager getUserManager() {
        return userManager;
    }
    @Override
    public synchronized void sendMessage(String message) throws RemoteException {
        messages.add(message);
    }

    @Override
    public String receiveMessage() throws RemoteException {
        try {
            return messages.take(); // This will block until a message is available
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public UserRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager = userManager;
    }

    protected synchronized void notifyListeners(UserManager userManager,String username,String message)throws RemoteException {
        for (IUserlistListener listener : listeners) {
            listener.updateUserManager(userManager);
        }
        for (IUserlistListener listener : listeners) {
            if (listener.getListenerName().equals(username)){
                listener.kick(message);
            }
        }
    }
    protected synchronized void notifyListeners(UserManager userManager)throws RemoteException {
        for (IUserlistListener listener : listeners) {
            listener.updateUserManager(userManager);
        }
    }
    protected synchronized void notifyListeners(String username)throws RemoteException {
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
    public synchronized void createAdmin(User user) throws RemoteException {

            System.out.println("add new admin:"+user.getUsername());
            userManager.addUser(user);
            userManager.setAdminUser(user);
            notifyListeners(userManager);
    }

    @Override
    public synchronized void createUser(User user) throws RemoteException {
        System.out.println("add new user:"+user.getUsername());
        userManager.addUser(user);
        notifyListeners(userManager);
    }

    @Override
    public void kickUser(String username,String message) throws RemoteException{
        System.out.println("kick user:"+username);
        userManager.deleteUser(username);
        notifyListeners(userManager,username,message);
    }

    @Override
    public synchronized void joinUser(String username) throws RemoteException {
        System.out.println(username+" want to join");
        for (int i = 0; i < userManager.getUsers().size(); i++) {
            if (username.equals(userManager.getUsers().get(i).getUsername())){
                for (IUserlistListener listener : listeners) {
                        listener.sameUsername(username);
                }
            }
        }
        notifyListeners(username);
    }

    @Override
    public synchronized boolean isUserAdmin(String username) throws RemoteException {
        for (int i = 0; i < userManager.getUsers().size(); i++) {
            if (userManager.isAdmin(username)){
                return true;
            }
        }
        return false;
    }

}
