package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.datamodel.*;
import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Queue;

public interface IWhiteboard extends Remote, Serializable {
    /**
     * Receives a rectangle drawn on the front-end, stores it on the server, and notifies
     * all listeners to draw the rectangle on their interfaces.
     *
     * @param rectangle The Rectangle object to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void drawRect(Rectangle rectangle) throws RemoteException;
    /**
     * Receives a circle drawn on the front-end, stores it on the server, and notifies
     * all listeners to draw the circle on their interfaces.
     *
     * @param circle The Circle object to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void drawCircle(Circle circle) throws RemoteException;
    /**
     * Receives a triangle drawn on the front-end, stores it on the server, and notifies
     * all listeners to draw the triangle on their interfaces.
     *
     * @param triangle The Triangle object to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void drawTriangle(Triangle triangle) throws RemoteException;
    /**
     * Receives an oval drawn on the front-end, stores it on the server, and notifies
     * all listeners to draw the oval on their interfaces.
     *
     * @param oval The Oval object to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void drawOval(Oval oval) throws RemoteException;
    /**
     * Receives a freehand drawing on the front-end, stores it on the server, and notifies
     * all listeners to replicate the drawing on their interfaces.
     *
     * @param line The Line object representing the freehand drawing to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void freeDraw(Line line) throws RemoteException;
    /**
     * Receives a text item drawn on the front-end, stores it on the server, and notifies
     * all listeners to display the text on their interfaces.
     *
     * @param text The TextItem object to be stored and drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void drawText(TextItem text) throws RemoteException;
    /**
     * Adds a listener for whiteboard events.
     *
     * @param listener The IWhiteboardListener to be added.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void addWhiteboardListener(IWhiteboardListener listener) throws RemoteException;
    /**
     * Retrieves the UserManager instance.
     *
     * @return The UserManager instance.
     * @throws RemoteException if there is an error during the remote method call.
     */
    UserManager getUserManager()throws RemoteException;
    /**
     * Checks if a user exists on the whiteboard.
     *
     * @param username The username of the user to check.
     * @return true if the user exists, false otherwise.
     * @throws RemoteException if there is an error during the remote method call.
     */
    boolean isUserExists(String username)throws RemoteException;
    /**
     * Loads the current state of the canvas on the back-end.
     *
     * @return A queue of Shape objects representing the current state of the canvas.
     * @throws RemoteException if there is an error during the remote method call.
     */
    Queue<Shape> loadCanvas() throws RemoteException ;
    /**
     * Saves the current state of the canvas as a dat file.
     *
     * @throws RemoteException if there is an error during the remote method call.
     */
    void saveCanvas()throws RemoteException;
    /**
     * Saves the current state of the canvas to a specified file path.
     *
     * @param filePath The file path where the canvas state should be saved.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void saveAsCanvas(String filePath)throws RemoteException;
    /**
     * Opens a file and loads its content onto the canvas.
     *
     * @param filePath The file path of the file to be opened.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void openFile(String filePath)throws RemoteException;
    /**
     * Reset the canvas to its initial state.
     *
     * @throws RemoteException if there is an error during the remote method call.
     */
    void newFile()throws RemoteException;
}
