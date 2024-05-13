package com.example.whiteboardclient;

import com.example.whiteboardclient.datamodel.UserManager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {

    public static void main(String[] args) {
        try {
            UserManager userManager = new UserManager();
            ChatRMI chatServer = new ChatRMI(userManager);
            UserRMI userRMI = new UserRMI(userManager);
            WhiteboardRMI whiteboardServer = new WhiteboardRMI(userManager);
            Registry registry = LocateRegistry.createRegistry(20017);
            registry.rebind("WhiteboardServer", whiteboardServer);
            registry.rebind("ChatServer", chatServer);
            registry.rebind("UserServer", userRMI);
            System.out.println("Whiteboard Server is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        if (args.length != 3) {
            String serverIPAddress = "localhost";
            int serverPort = 20017;
            String username = "admin";

            System.err.println("Usage: java CreateWhiteBoard <serverIPAddress> <serverPort> <username>");
            WhiteBoardApplication.startWhiteBoard(serverIPAddress, serverPort, username,true);

            //System.exit(1);
        }else{
            String serverIPAddress = args[0];
            int serverPort = Integer.parseInt(args[1]);
            String username = args[2];
            WhiteBoardApplication.startWhiteBoard(serverIPAddress, serverPort, username,true);
        }

    }
}
