package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private boolean isActive;
    private String ipAddress;

    public User(String username) {
        this.username = username;
        this.isActive = true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
