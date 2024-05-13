package com.example.whiteboardclient.listener;

import com.example.whiteboardclient.WhiteboardUIUpdater;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import com.example.whiteboardclient.datamodel.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardListener extends UnicastRemoteObject implements IWhiteboardListener, Serializable {
    private WhiteboardUIUpdater uiUpdater;

    public WhiteboardListener(WhiteboardUIUpdater uiUpdater) throws RemoteException {
        super();
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void rectDrawn(Rectangle rectangle) throws RemoteException {
        uiUpdater.displayRect(rectangle);
    }

    @Override
    public void lineDrawn(Line line) throws RemoteException {
        uiUpdater.displayLine(line);
    }

    @Override
    public void triangleDrawn(Triangle triangle) throws RemoteException {
        uiUpdater.displayTriangle(triangle);
    }

    @Override
    public void circleDrawn(Circle circle) throws RemoteException {
        uiUpdater.displayCircle(circle);
    }

    @Override
    public void ovalDrawn(Oval oval) throws RemoteException {
        uiUpdater.displayOval(oval);
    }

    @Override
    public void textDrawn(TextItem text) throws RemoteException {
        uiUpdater.displayText(text);
    }

    @Override
    public void updateCanvas() throws RemoteException {
        uiUpdater.updateCanvas();
    }

}
