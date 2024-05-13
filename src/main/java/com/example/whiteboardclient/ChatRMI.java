package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.IChatListener;
import com.example.whiteboardclient.listener.IUserlistListener;
import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRMI extends UnicastRemoteObject implements IChatService, Serializable {
    private UserManager userManager;
    private List<IChatListener> listeners = new CopyOnWriteArrayList<>();

    protected ChatRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager = userManager;
    }

    protected void notifyListeners(String username,String message)throws RemoteException {
        for (IChatListener listener : listeners) {
            listener.sendMessageToEveryone(username,message);
        }
    }

    @Override
    public void sendMessage(String username,String message) throws RemoteException {
        System.out.println(username+" send message: "+message);
        notifyListeners(username,message);

    }

    @Override
    public void addChatListener(IChatListener listener) throws RemoteException {
        listeners.add(listener);
    }

}
