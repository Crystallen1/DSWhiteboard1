package com.example.whiteboardclient.connect;

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

/**
 * ChatRMI is responsible for managing chat functionality in a remote method invocation (RMI) setting.
 * It maintains a list of listeners to which messages are broadcasted.
 */
public class ChatRMI extends UnicastRemoteObject implements IChatService, Serializable {
    private UserManager userManager;
    private List<IChatListener> listeners = new CopyOnWriteArrayList<>();

    public ChatRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager = userManager;
    }
    /**
     * Notifies all registered listeners with the provided message from the given username.
     *
     * @param username The name of the user sending the message.
     * @param message The message to be sent.
     * @throws RemoteException if there is an error during the remote method call.
     */
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
