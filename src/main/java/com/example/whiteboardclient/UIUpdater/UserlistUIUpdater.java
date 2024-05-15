package com.example.whiteboardclient.UIUpdater;


import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

public interface UserlistUIUpdater {
    /**
     * Adds a user to the user list in the UI.
     *
     * @param userManager The UserManager instance containing user information.
     */
    void addUserToList(UserManager userManager);
    /**
     * Displays a request dialog to the manager.
     *
     * @param username The username of the user want to join.
     */
    void showRequestDialog(String username);
    /**
     * Shuts down the UI with a given message.
     *
     * @param message The message to be displayed during the shutdown process.
     */
    void shutdown(String message);
}

