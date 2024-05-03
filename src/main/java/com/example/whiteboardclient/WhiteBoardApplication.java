package com.example.whiteboardclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.IWhiteboard;

import java.net.MalformedURLException;
import java.rmi.Naming;  // 导入Naming类

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class WhiteBoardApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        try {
            String remoteObjectName = "//localhost:20017/WhiteboardServer";
            IWhiteboard service = (IWhiteboard) Naming.lookup(remoteObjectName);
            // 现在可以使用 service 对象调用远程方法了
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(WhiteBoardApplication.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}