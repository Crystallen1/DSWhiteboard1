package com.example.whiteboardclient.listener;

import com.example.whiteboardclient.datamodel.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWhiteboardListener extends Remote, Serializable {
    /**
     * Called to draw a rectangle on connected whiteboard interfaces.
     *
     * @param rectangle The Rectangle object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void rectDrawn(Rectangle rectangle) throws RemoteException;
    /**
     * Called to draw a line on connected whiteboard interfaces.
     *
     * @param line The Line object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void lineDrawn(Line line) throws RemoteException;
    /**
     * Called to draw a triangle on connected whiteboard interfaces.
     *
     * @param triangle The Triangle object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void triangleDrawn(Triangle triangle)throws RemoteException;
    /**
     * Called to draw a circle on connected whiteboard interfaces.
     *
     * @param circle The Circle object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void circleDrawn(Circle circle)throws RemoteException;
    /**
     * Called to draw an oval on connected whiteboard interfaces.
     *
     * @param oval The Oval object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void ovalDrawn(Oval oval)throws RemoteException;
    /**
     * Called to draw a text item on connected whiteboard interfaces.
     *
     * @param text The TextItem object to be drawn.
     * @throws RemoteException if there is an error during the remote method call.
     */
    void textDrawn(TextItem text) throws RemoteException;
    /**
     * Called to update the entire canvas on all connected whiteboard interfaces.
     * Typically used when a major change occurs.
     *
     * @throws RemoteException if there is an error during the remote method call.
     */
    void updateCanvas()throws RemoteException;
}
