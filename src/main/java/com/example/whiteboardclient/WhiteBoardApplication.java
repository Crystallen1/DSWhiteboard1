package com.example.whiteboardclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;  // 导入Naming类

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WhiteBoardApplication extends Application implements Serializable {
    static String serverIPAddress;
    static int serverPort;
    static String username;

    static boolean admin;

    public static String getUsername() {
        return username;
    }

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        System.out.println(serverIPAddress+serverPort+username);
        FXMLLoader fxmlLoader = new FXMLLoader(WhiteBoardApplication.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void startWhiteBoard(String serverIPAddress, int serverPort, String username, boolean admin) {
        WhiteBoardApplication.serverIPAddress = serverIPAddress;
        WhiteBoardApplication.serverPort = serverPort;
        WhiteBoardApplication.username = username;
        WhiteBoardApplication.admin = admin;
        launch();
    }

    public static void main(String[] args) {

        launch();
    }
}

