package com.example.whiteboardclient;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.example.IWhiteboardListener;
import org.example.Line;
import org.example.Shape;
import org.example.TextItem;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardListener extends UnicastRemoteObject implements IWhiteboardListener, Serializable {
    private UIUpdater uiUpdater;

    public WhiteboardListener(UIUpdater uiUpdater) throws RemoteException {
        super();
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void shapeDrawn(Shape shape) throws RemoteException {

    }

    @Override
    public void lineDrawn(Line line) throws RemoteException {
        uiUpdater.displayLine(line);
    }

    @Override
    public void textDrawn(TextItem text) throws RemoteException {

    }
}
