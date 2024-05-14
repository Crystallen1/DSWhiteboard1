package com.example.whiteboardclient.UIUpdater;


import com.example.whiteboardclient.datamodel.*;

import java.rmi.RemoteException;

public interface WhiteboardUIUpdater {
     void displayLine(Line line);
     void displayTriangle(Triangle triangle);
     void displayCircle(Circle circle);
     void displayOval(Oval oval);
     void displayText(TextItem textItem);
     void displayRect(Rectangle rectangle);
     void updateCanvas()throws RemoteException;

}
