package com.example.whiteboardclient.datamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserManager implements Serializable {
    private List<User> users;
    private User adminUser;

    public UserManager() {
        this.users = new ArrayList<>();
        this.adminUser = null;
    }

    public void deleteUser(String username){
        for (int i = 0; i < users.size(); i++) {
            if (Objects.equals(users.get(i).getUsername(), username)) {
                users.remove(i);
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

    public boolean isAdmin(String username) {
        return Objects.equals(username, adminUser.getUsername());
    }
}
