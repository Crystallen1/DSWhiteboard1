package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.controller.MainController;
import com.example.whiteboardclient.datamodel.UserManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;  // 导入Naming类

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WhiteBoardApplication extends Application implements Serializable {
    static String serverIPAddress;
    static int serverPort;
    static String username;
    static boolean admin;
    private static final long HEARTBEAT_INTERVAL = 5000; // 5 seconds
    private static boolean running = true;

    public static String getUsername() {
        return username;
    }

    public static String getServerIPAddress() {
        return serverIPAddress;
    }

    public static int getServerPort() {
        return serverPort;
    }

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        System.out.println(serverIPAddress+serverPort+username);

        FXMLLoader fxmlLoader = new FXMLLoader(WhiteBoardApplication.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("WhiteBoard");
        stage.setScene(scene);
        stage.show();

        IUserlist userlistServer;

        String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/UserServer";
        userlistServer = (IUserlist) Naming.lookup(remoteObjectName);
        // heartbeat
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    userlistServer.heartbeat();
                    System.out.println("still connect");
                } catch (RemoteException e) {
                    running = false;
                    System.out.println("connect broke");
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "manager close the server");
                        alert.showAndWait();
                        stage.close();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        stage.setOnCloseRequest(event -> {
            if (isAdmin()){
                UserManager userManager= null;
                try {
                    userManager = userlistServer.getUserManager();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < userManager.getUsers().size(); i++) {
                    String username =userManager.getUsers().get(i).getUsername();
                    try {
                        userlistServer.kickUser(username,"The manager close the server");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Kicking user: " + username);
                }
                System.exit(0);
            }else {
                try {
                    userlistServer.kickUser(WhiteBoardApplication.getUsername(),"Quit Successful");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            // 关闭 JavaFX 应用程序
            Platform.exit();
        });
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void startWhiteBoard(String serverIPAddress, int serverPort, String username, boolean admin) {
        WhiteBoardApplication.serverIPAddress = serverIPAddress;
        WhiteBoardApplication.serverPort = serverPort;
        WhiteBoardApplication.username = username;
        WhiteBoardApplication.admin = admin;
        Locale.setDefault(new Locale("en", "US")); // 设置默认语言为英文

        launch();
    }
}

