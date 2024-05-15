package com.example.whiteboardclient.listener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatListener extends Remote, Serializable {
    /**
     * Sends a message to all users in the chat.
     *
     * @param username The username of the user sending the message.
     * @param message The message to be sent to all users.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void sendMessageToEveryone(String username,String message)throws RemoteException;
}
