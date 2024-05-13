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
    private IChatService chatService;

    private IWhiteboard whiteboardServer;
    public void initialize() throws IOException {
        try {
            String remoteObjectName = "//localhost:20017/UserServer";
            // RMI 服务器查找
            userlistServer = (IUserlist) Naming.lookup(remoteObjectName);
//            UserlistListener listener = new UserlistListener(this,WhiteBoardApplication.isAdmin());
//            server.addUserlistListener(listener);
            if (WhiteBoardApplication.isAdmin()){
                userlistServer.createAdmin(new User(WhiteBoardApplication.getUsername()));
//                server.receiveMessage();
                onLoginSuccess();
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
            remoteObjectName = "//localhost:20017/WhiteboardServer";
            // RMI 服务器查找
            whiteboardServer = (IWhiteboard) Naming.lookup(remoteObjectName);
            remoteObjectName = "//localhost:20017/ChatServer";
            // RMI 服务器查找
            chatService = (IChatService) Naming.lookup(remoteObjectName);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
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

    public void handleNew(ActionEvent actionEvent) throws RemoteException {
        whiteboardServer.newFile();
    }

    public void handleOpen(ActionEvent actionEvent) throws RemoteException {
        FileChooser fileChooser = new FileChooser();
        // 设置窗口的标题
        fileChooser.setTitle("选择.dat文件");

        // 设置文件过滤器，只允许.dat格式的文件
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        // 打开文件选择窗口，并获取用户选择的文件
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // 打印出选择的文件路径，或者进行其他操作
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
            // 这里可以添加代码来处理选择的文件，例如读取文件内容等
            whiteboardServer.openFile(selectedFile.getAbsolutePath());
            //whiteboardServer.loadCanvas();
        }
    }

    public void handleSave(ActionEvent actionEvent) throws RemoteException {
        whiteboardServer.saveCanvas();
    }

    public void handleSaveAs(ActionEvent actionEvent) throws RemoteException {
        // 创建并配置DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a Directory");

        // 打开选择目录的窗口
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            // 创建一个TextInputDialog，提示用户输入文件名
            TextInputDialog textInputDialog = new TextInputDialog("defaultFileName");
            textInputDialog.setTitle("File Name Input");
            textInputDialog.setHeaderText("Enter the file name:");
            textInputDialog.setContentText("Name:");

            // 显示对话框并等待用户响应
            Optional<String> result = textInputDialog.showAndWait();

            // 检查用户是否输入了文件名
            if (result.isPresent()) {
                String fileName = result.get();
                // 构建完整的文件路径
                File file = new File(selectedDirectory.getAbsolutePath() + File.separator + fileName + ".dat");

                // 调用whiteboardServer的saveAsCanvas方法，传入文件路径
                whiteboardServer.saveAsCanvas(file.getAbsolutePath());
            }
        }
    }

    public void handleClose(ActionEvent actionEvent) throws RemoteException {
        UserManager userManager=userlistServer.getUserManager();
        for (int i = 0; i < userManager.getUsers().size(); i++) {
            String username =userManager.getUsers().get(i).getUsername();
            userlistServer.kickUser(username);
            System.out.println("Kicking user: " + username);
        }
        System.exit(0);
    }
}
