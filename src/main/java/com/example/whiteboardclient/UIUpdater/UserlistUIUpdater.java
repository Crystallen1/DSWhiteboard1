package com.example.whiteboardclient.UIUpdater;


import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;

public interface UserlistUIUpdater {
    void addUserToList(UserManager userManager);
    Boolean showRequestDialog(String username);
    void warnSameUsername(String username);
    void shutdown(String message);
}

