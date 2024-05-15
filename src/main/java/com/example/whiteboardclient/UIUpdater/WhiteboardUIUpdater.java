package com.example.whiteboardclient.UIUpdater;


import com.example.whiteboardclient.datamodel.*;

import java.rmi.RemoteException;

public interface WhiteboardUIUpdater {
     /**
      * Displays a line on the canvas.
      *
      * @param line The Line object to be displayed.
      */
     void displayLine(Line line);
     /**
      * Displays a triangle on the canvas.
      *
      * @param triangle The Triangle object to be displayed.
      */
     void displayTriangle(Triangle triangle);
     /**
      * Displays a circle on the canvas.
      *
      * @param circle The Circle object to be displayed.
      */
     void displayCircle(Circle circle);
     /**
      * Displays an oval on the canvas.
      *
      * @param oval The Oval object to be displayed.
      */
     void displayOval(Oval oval);
     /**
      * Displays a text item on the canvas.
      *
      * @param textItem The TextItem object to be displayed.
      */
     void displayText(TextItem textItem);
     /**
      * Displays a rectangle on the canvas.
      *
      * @param rectangle The Rectangle object to be displayed.
      */
     void displayRect(Rectangle rectangle);
     /**
      * Updates the canvas to reflect the all changes.
      *
      * @throws RemoteException if there is an error during the remote method call.
      */
     void updateCanvas()throws RemoteException;
}
