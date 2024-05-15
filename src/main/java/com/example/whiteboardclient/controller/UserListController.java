package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.UIUpdater.UserlistUIUpdater;
import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.UserlistListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class UserListController implements Serializable,UserlistUIUpdater {

    public ListView<String> userList;
    private IUserlist server; // RMI interface


    private void kickUser(String username) throws RemoteException {
        if (server.isUserAdmin(username)){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can't kick yourself");
                alert.showAndWait();
            });
        }else {
            server.kickUser(username,"You have been kicked out");
            System.out.println("Kicking user: " + username);
        }
    }
    public void addUser(String username) {
        ObservableList<String> items = userList.getItems();
        items.add(username);
    }

    @Override
    public void addUserToList(UserManager userManager) {
        Platform.runLater(() -> {
            userList.getItems().clear();
            for (int i = 0; i < userManager.getUsers().size(); i++) {
                addUser(userManager.getUsers().get(i).getUsername());
            }
        });
    }

    @Override
    public void showRequestDialog(String username) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Allow user \"" + username + "\" to join?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        // Check if a user with the same username already exists in the user list
                        for (int i = 0; i < userList.getItems().size(); i++) {
                            if (userList.getItems().get(i).equals(username)){
                                // If a user with the same username exists, send a "same name" message to the server and exit
                                server.sendMessage("same name");
                                return;
                            }
                        }
                        // If no user with the same username exists, create a new user and send an "approve" message to the server
                        server.createUser(new User(username));
                        server.sendMessage("approve");
                    } catch (RemoteException e) {
                        System.err.println("error: " + e.getMessage());
                        e.printStackTrace();                    }
                } else {
                    // If the response is NO, send a "disapprove" message to the server
                    try {
                        server.sendMessage("disapprove");
                    } catch (RemoteException e) {
                        System.err.println("error: " + e.getMessage());
                        e.printStackTrace();                    }
                }
            });
        });
    }

    @Override
    public void shutdown(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.showAndWait();
            Stage stage = (Stage) userList.getScene().getWindow();
            stage.close();
        });
    }

    public void initialize() throws RemoteException {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/UserServer";
            server = (IUserlist) Naming.lookup(remoteObjectName);
            UserlistListener listener = new UserlistListener(this,WhiteBoardApplication.isAdmin(),WhiteBoardApplication.getUsername());
            server.addUserlistListener(listener);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }

        userList.setCellFactory(param -> new ListCell<String>() {
            private final HBox hBox = new HBox();
            private final Text text = new Text();
            private final Button button = new Button("Kick");

            {
                hBox.setSpacing(100);
                if (WhiteBoardApplication.isAdmin()){
                    hBox.getChildren().addAll(text, button);
                }else {
                    hBox.getChildren().addAll(text);
                }
                button.setOnAction(event -> {
                    try {
                        kickUser(getItem());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(hBox);
                }
            }
        });
        addUserToList(server.getUserManager());
    }
}
