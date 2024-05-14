package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.UserlistUIUpdater;
import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.UserlistListener;
import com.example.whiteboardclient.listener.WhiteboardListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class UserListController implements Serializable,UserlistUIUpdater {

    public ListView<String> userList;
    private IUserlist server; // RMI 服务接口


    private void kickUser(String username) throws RemoteException {
        if (server.isUserAdmin(username)){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "you can't kick yourself");
                alert.showAndWait();
            });
        }else {
            server.kickUser(username,"You have been kicked");
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
    public Boolean showRequestDialog(String username) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Allow user " + username + " to join?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        server.createUser(new User(username));
                        server.sendMessage("approve");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        server.sendMessage("disapprove");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        return true;
    }

    @Override
    public void warnSameUsername(String username) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "same username");
            alert.showAndWait();
        });
    }

    @Override
    public void updateUserListDisconnected(String username) {

    }

    @Override
    public void shutdown(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.showAndWait();
            Stage stage = (Stage) userList.getScene().getWindow();
            stage.close();
//            System.exit(0);  // 或者其他适当的停止机制
        });
    }

    public void initialize() throws RemoteException {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/UserServer";
            // RMI 服务器查找
            server = (IUserlist) Naming.lookup(remoteObjectName);
            UserlistListener listener = new UserlistListener(this,WhiteBoardApplication.isAdmin(),WhiteBoardApplication.getUsername());
            server.addUserlistListener(listener);
//            if (WhiteBoardApplication.isAdmin()){
//                server.createAdmin(new User(WhiteBoardApplication.getUsername()));
//            }else {
//                //server.createAdmin(new User(WhiteBoardApplication.getUsername()));
//                server.joinUser(WhiteBoardApplication.getUsername());
//            }
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
            // 显示错误弹出窗口
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            // Optional: 关闭主窗口
            System.exit(1);
        }

        userList.setCellFactory(param -> new ListCell<String>() {
            private final HBox hBox = new HBox();
            private final Text text = new Text();
            private final Button button = new Button("Kick");

            {
                hBox.setSpacing(100); // 设置间距
                if (WhiteBoardApplication.isAdmin()){
                    hBox.getChildren().addAll(text, button); // 添加文本和按钮到水平布局
                }else {
                    hBox.getChildren().addAll(text);
                }
                button.setOnAction(event -> {
                    try {
                        kickUser(getItem());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }); // 为按钮设置动作
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); // 如果项是空的，不显示任何内容
                } else {
                    text.setText(item); // 设置文本为项的内容
                    setGraphic(hBox); // 设置图形为HBox
                }
            }
        });
        addUserToList(server.getUserManager());
    }
}
