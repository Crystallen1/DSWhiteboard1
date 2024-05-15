package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.listener.IChatListener;
import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatService extends Remote {
    /**
     * Handles sending a message from a user. This includes logging the message
     * and notifying all listeners.
     *
     * @param username The name of the user sending the message.
     * @param message The message to be sent.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void sendMessage(String username,String message) throws RemoteException;
    /**
     * Adds a new listener to the list of listeners.
     *
     * @param listener The IChatListener to be added.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void addChatListener(IChatListener listener) throws RemoteException;
}
