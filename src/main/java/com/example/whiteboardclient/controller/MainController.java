package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.UserlistListener;
import com.example.whiteboardclient.listener.WhiteboardListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class MainController {

    @FXML
    public BorderPane mainPane;
    @FXML
    public MenuBar menuBar;
    private IUserlist userlistServer;

    public void initialize() throws IOException {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/UserServer";
            // RMI 服务器查找
            userlistServer = (IUserlist) Naming.lookup(remoteObjectName);
//            UserlistListener listener = new UserlistListener(this,WhiteBoardApplication.isAdmin());
//            server.addUserlistListener(listener);
            if (WhiteBoardApplication.isAdmin()){
                userlistServer.createAdmin(new User(WhiteBoardApplication.getUsername()));
//                server.receiveMessage();
                onLoginSuccess();
                loadMenuBar();
            }else {
                userlistServer.joinUser(WhiteBoardApplication.getUsername());
                if ("approve".equals(userlistServer.receiveMessage())){
                    onLoginSuccess();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"you are not approve!");
                    alert.showAndWait();
//                    Stage stage = (Stage) mainPane.getScene().getWindow();
//                    stage.close();
                }
            }

        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
            // 显示错误弹出窗口
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            // Optional: 关闭主窗口
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.close();
        }
    }
    private void onLoginSuccess() throws IOException {
        // 动态加载子界面
        loadUserList();
        loadWhiteboard();
        loadChat();
    }
    private void loadUserList() throws IOException {
        FXMLLoader loader = new FXMLLoader(WhiteBoardApplication.class.getResource("UserList.fxml"));
        Node userList = loader.load();
        mainPane.setLeft(userList);
    }

    private void loadWhiteboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(WhiteBoardApplication.class.getResource("Whiteboard.fxml"));
        Node whiteboard = loader.load();
        mainPane.setCenter(whiteboard);
    }

    private void loadChat() throws IOException {
        FXMLLoader loader = new FXMLLoader(WhiteBoardApplication.class.getResource("Chat.fxml"));
        Node chat = loader.load();
        mainPane.setRight(chat);
    }
    private void loadMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(WhiteBoardApplication.class.getResource("MenuBar.fxml"));
            MenuBar menuBar = loader.load();
            mainPane.setTop(menuBar); // 将菜单栏设置到BorderPane的顶部
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleCloseMy(ActionEvent actionEvent) throws RemoteException {
        userlistServer.kickUser(WhiteBoardApplication.getUsername(),"Quit Successful");
        System.out.println("Kicking user: " + WhiteBoardApplication.getUsername());
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }
}
