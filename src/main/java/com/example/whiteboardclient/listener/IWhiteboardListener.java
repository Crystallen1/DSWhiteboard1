package com.example.whiteboardclient.listener;

import com.example.whiteboardclient.datamodel.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWhiteboardListener extends Remote, Serializable {
    void rectDrawn(Rectangle rectangle) throws RemoteException;
    void lineDrawn(Line line) throws RemoteException;
    void triangleDrawn(Triangle triangle)throws RemoteException;
    void circleDrawn(Circle circle)throws RemoteException;
    void ovalDrawn(Oval oval)throws RemoteException;
    void textDrawn(TextItem text) throws RemoteException;

}
