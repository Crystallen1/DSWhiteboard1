package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IChatService;
import com.example.whiteboardclient.connect.IUserlist;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.User;
import com.example.whiteboardclient.datamodel.UserManager;
import com.example.whiteboardclient.listener.ChatListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

public class MenuBarController implements Serializable {
    public MenuBar menuBar;
    private IUserlist userlistServer;
    private IChatService chatService;

    private IWhiteboard whiteboardServer;
    public void initialize() {
        try {
            String remoteObjectName = "//"+ WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/UserServer";
            userlistServer = (IUserlist) Naming.lookup(remoteObjectName);
            remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/WhiteboardServer";
            whiteboardServer = (IWhiteboard) Naming.lookup(remoteObjectName);
            remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/ChatServer";
            chatService = (IChatService) Naming.lookup(remoteObjectName);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();
            System.exit(1);
        }
    }
    public void handleNew(ActionEvent actionEvent) throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save current canvas", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    whiteboardServer.saveCanvas();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    whiteboardServer.newFile();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    whiteboardServer.newFile();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void handleOpen(ActionEvent actionEvent) throws RemoteException {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose *.dat file");

        // Set a file extension filter to only allow .dat files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        // Open the file chooser dialog and get the user's selected file
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
            // Call the openFile method on the whiteboard server with the selected file's path
            whiteboardServer.openFile(selectedFile.getAbsolutePath());
            //whiteboardServer.loadCanvas();
        }
    }

    public void handleSave(ActionEvent actionEvent) throws RemoteException {
        try {
            whiteboardServer.saveCanvas();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful saved!");
            alert.showAndWait();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed: " + e.getMessage());
            alert.showAndWait();
        }

    }

    public void handleSaveAs(ActionEvent actionEvent) throws RemoteException {
        // Create and configure the DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a Directory");

        // Open the directory chooser dialog window
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            // Create a TextInputDialog to prompt the user to enter a file name
            TextInputDialog textInputDialog = new TextInputDialog("defaultFileName");
            textInputDialog.setTitle("File Name Input");
            textInputDialog.setHeaderText("Enter the file name:");
            textInputDialog.setContentText("Name:");

            // Show the dialog and wait for the user's response
            Optional<String> result = textInputDialog.showAndWait();

            // Check if the user entered a file name
            if (result.isPresent()) {
                String fileName = result.get();
                // Construct the full file path
                File file = new File(selectedDirectory.getAbsolutePath() + File.separator + fileName + ".dat");
                // Call the saveAsCanvas method of whiteboardServer, passing the file path
                whiteboardServer.saveAsCanvas(file.getAbsolutePath());
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter the file name");
                alert.showAndWait();
            }
        }
    }

    public void handleClose(ActionEvent actionEvent) throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save current canvas", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // If the user chooses 'YES', save the current canvas
                try {
                    whiteboardServer.saveCanvas();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                UserManager userManager= null;
                try {
                    userManager = userlistServer.getUserManager();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                // Iterate through the list of users and kick each user with a notification message
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
            } else {
                // If the user chooses 'NO', only kick all users without saving the canvas
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
            }
        });

    }
}
