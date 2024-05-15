package com.example.whiteboardclient.listener;


import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserlistListener extends Remote, Serializable {
    /**
     * Updates the UserManager instance with the latest state.
     *
     * @param userManager The updated UserManager instance.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void updateUserManager(UserManager userManager)throws RemoteException;
    /**
     * Handles a request from a user to join the chat.
     *
     * @param username The username of the user requesting to join.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void joinRequest(String username)throws RemoteException;
    /**
     * Checks if the listener is an admin.
     *
     * @return true if the listener is an admin, false otherwise.
     * @throws RemoteException if there is an error during the remote method call.
     */
    boolean getAdmin()throws RemoteException;
    /**
     * Retrieves the username of the listener.
     *
     * @return The name of the listener.
     * @throws RemoteException if there is an error during the remote method call.
     */
    String getListenerName()throws RemoteException;
    /**
     * Kicks a user from the userlist with a given message.
     *
     * @param message The message to be sent to the kicked user.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void kick(String message)throws RemoteException;
}
