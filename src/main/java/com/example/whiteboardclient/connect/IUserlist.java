package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.IUserlistListener;
import com.example.whiteboardclient.listener.UserlistListener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IUserlist extends Remote, Serializable {
    /**
     * Adds a listener for user list events.
     *
     * @param listener The IUserlistListener to be added.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void addUserlistListener(IUserlistListener listener) throws RemoteException;
    /**
     * Creates an admin user.
     *
     * @param user The User object representing the admin to be created.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void createAdmin(User user) throws RemoteException;
    /**
     * Creates a regular user.
     *
     * @param user The User object representing the user to be created.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void createUser(User user) throws RemoteException;
    /**
     * Kicks a user out of the chat with a given message.
     *
     * @param username The username of the user to be kicked.
     * @param message The message to be sent to the kicked user.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void kickUser(String username,String message)throws RemoteException;
    /**
     * Allows a user to join the chat.
     *
     * @param username The username of the user joining the chat.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void joinUser(String username)throws RemoteException;
    /**
     * Retrieves the UserManager instance.
     *
     * @return The UserManager instance.
     * @throws RemoteException if there is an error during the remote method call.
     */
    UserManager getUserManager() throws RemoteException;
    /**
     * Sends a message to Server.
     *
     * @param message The message to be sent.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void sendMessage(String message) throws RemoteException;
    /**
     * Receives a message from the Server.
     *
     * @return The received message.
     * @throws RemoteException if there is an error during the remote method call.
     */
    String receiveMessage() throws RemoteException;
    /**
     * Checks if a user is an admin.
     *
     * @param username The username of the user to check.
     * @return true if the user is an admin, false otherwise.
     * @throws RemoteException if there is an error during the remote method call.
     */
    boolean isUserAdmin(String username)throws RemoteException;

    void heartbeat()throws RemoteException;
}
