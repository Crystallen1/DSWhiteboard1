package com.example.whiteboardclient;

public class JoinWhiteBoard {
    public static void main(String[] args) {
//        if (args.length != 3) {
//            System.err.println("Usage: java JoinWhiteBoard <serverIPAddress> <serverPort> <username>");
//            System.exit(1);
//        }
//
//        String serverIPAddress = args[0];
//        int serverPort = Integer.parseInt(args[1]);
//        String username = args[2];

        WhiteBoardApplication.startWhiteBoard("serverIPAddress", 1, "username",false);
    }
}
