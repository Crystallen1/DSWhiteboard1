package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.ChatRMI;
import com.example.whiteboardclient.connect.UserRMI;
import com.example.whiteboardclient.connect.WhiteboardRMI;
import com.example.whiteboardclient.datamodel.UserManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {

    public static void main(String[] args) {


        if (args.length != 3) {
            // Default values for server IP address, port, and username
            String serverIPAddress = "localhost";
            int serverPort = 20017;
            String username = "cherrim";
            try {
                // Create instances of UserManager and the RMI servers
                UserManager userManager = new UserManager();
                ChatRMI chatServer = new ChatRMI(userManager);
                UserRMI userRMI = new UserRMI(userManager);
                WhiteboardRMI whiteboardServer = new WhiteboardRMI(userManager);

                // Create the RMI registry on the specified port and bind the servers to it
                Registry registry = LocateRegistry.createRegistry(serverPort);
                registry.rebind("WhiteboardServer", whiteboardServer);
                registry.rebind("ChatServer", chatServer);
                registry.rebind("UserServer", userRMI);
                System.out.println("Whiteboard Server is ready.");
            } catch (Exception e) {
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
            }
            System.err.println("Usage: java CreateWhiteBoard <serverIPAddress> <serverPort> <username>");
            WhiteBoardApplication.startWhiteBoard(serverIPAddress, serverPort, username,true);

            //System.exit(1);
        }else{
            // Use values provided as arguments
            String serverIPAddress = args[0];
            int serverPort = Integer.parseInt(args[1]);
            String username = args[2];
            try {
                // Create instances of UserManager and the RMI servers
                UserManager userManager = new UserManager();
                ChatRMI chatServer = new ChatRMI(userManager);
                UserRMI userRMI = new UserRMI(userManager);
                WhiteboardRMI whiteboardServer = new WhiteboardRMI(userManager);

                // Create the RMI registry on the specified port and bind the servers to it
                Registry registry = LocateRegistry.createRegistry(serverPort);
                registry.rebind("WhiteboardServer", whiteboardServer);
                registry.rebind("ChatServer", chatServer);
                registry.rebind("UserServer", userRMI);
                System.out.println("Whiteboard Server is ready.");
            } catch (Exception e) {
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
            }
            WhiteBoardApplication.startWhiteBoard(serverIPAddress, serverPort, username,true);
        }


    }
}
