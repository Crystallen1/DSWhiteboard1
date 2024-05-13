package com.example.whiteboardclient;

import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.*;
import com.example.whiteboardclient.listener.IWhiteboardListener;

//import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiteboardRMI extends UnicastRemoteObject implements IWhiteboard, Serializable {
    private List<IWhiteboardListener> listeners = new ArrayList<>();
    private UserManager userManager;

    CanvasStorage storage;


    protected WhiteboardRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager=userManager;
        this.storage= new CanvasStorage();
    }

    public synchronized void addWhiteboardListener(IWhiteboardListener listener) throws RemoteException {
        listeners.add(listener);
    }

    @Override
    public UserManager getUserManager() throws RemoteException {
        return userManager;
    }

    @Override
    public boolean isUserExists(String username) throws RemoteException {
        for (int i = 0; i < userManager.getUsers().size(); i++) {
            if (Objects.equals(userManager.getUsers().get(i).getUsername(), username)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Shape> loadCanvas()  throws RemoteException {
        return storage.getShapes();
    }

    @Override
    public void saveCanvas() throws RemoteException {
        storage.saveShapes();
    }

    @Override
    public void saveAsCanvas(String filePath)throws RemoteException {
        storage.saveAsShapes(filePath);
    }

    @Override
    public void openFile(String filePath) throws RemoteException {
        storage.loadShapesFromFile(filePath);
        for (IWhiteboardListener listener : listeners) {
            listener.updateCanvas();
        }
    }

    @Override
    public void newFile() throws RemoteException {
        storage.clearShape();
        for (IWhiteboardListener listener : listeners) {
            listener.updateCanvas();
        }
    }

    protected void notifyListeners(Rectangle rectangle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.rectDrawn(rectangle);
        }
    }

    protected void notifyListeners(TextItem text) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.textDrawn(text);
        }
    }

    protected void notifyListeners(Line line) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.lineDrawn(line);
        }
    }

    protected void notifyListeners(Triangle triangle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.triangleDrawn(triangle);
        }
    }
    protected void notifyListeners(Circle circle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.circleDrawn(circle);
        }
    }
    protected void notifyListeners(Oval oval) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.ovalDrawn(oval);
        }
    }


    @Override
    public void drawRect(Rectangle rectangle) throws RemoteException {
        System.out.println("Line drawn from (" + rectangle.getStartX() + ", " + rectangle.getStartY());
        notifyListeners(rectangle);
        storage.addShape(rectangle);
    }

    @Override
    public void drawCircle(Circle circle) throws RemoteException {
        System.out.println("Circle drawn from (" + circle.getStartX() + ", " + circle.getStartY());
        notifyListeners(circle);
        storage.addShape(circle);
    }

    @Override
    public void drawTriangle(Triangle triangle) throws RemoteException {
        System.out.println("triangle drawn from (" + triangle.getStartX() + ", " + triangle.getStartY());
        notifyListeners(triangle);
        storage.addShape(triangle);
    }

    @Override
    public void drawOval(Oval oval) throws RemoteException {
        System.out.println("oval drawn from (" + oval.getStartX() + ", " + oval.getStartY());
        notifyListeners(oval);
        storage.addShape(oval);
    }

    @Override
    public void freeDraw(Line line) throws RemoteException {
        System.out.println(line.getColor()+"Line drawn from (" + line.getStartX() + ", " + line.getStartY() + ") to (" + line.getEndX() + ", " + line.getEndY() + ")");
        notifyListeners(line);
        storage.addShape(line);
    }

    @Override
    public void drawText(TextItem text) throws RemoteException {
        System.out.println(text.getText()+" add at" + text.getStartX() + ", " + text.getStartY() + "with" +text.getColor());
        notifyListeners(text);
        storage.addShape(text);
    }

    @Override
    public void clear() throws RemoteException {

    }

    @Override
    public void disconnect() throws RemoteException {

    }
}
