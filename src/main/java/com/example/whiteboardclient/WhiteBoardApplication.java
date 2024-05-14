package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.controller.MainController;
import com.example.whiteboardclient.datamodel.UserManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        // RMI 服务器查找
        userlistServer = (IUserlist) Naming.lookup(remoteObjectName);
        stage.setOnCloseRequest(event -> {
            // 调用控制器中的断开连接方法
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
        launch();
    }
}

