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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WhiteboardRMI extends UnicastRemoteObject implements IWhiteboard, Serializable {
    private ExecutorService executorService;

    private List<IWhiteboardListener> listeners = new CopyOnWriteArrayList<>();
    private UserManager userManager;

    CanvasStorage storage;


    protected WhiteboardRMI(UserManager userManager) throws RemoteException {
        super();
        this.userManager=userManager;
        this.storage= new CanvasStorage();
        this.executorService = Executors.newFixedThreadPool(40); // 创建具有固定数目线程的线程池
    }

    public synchronized void addWhiteboardListener(IWhiteboardListener listener) throws RemoteException {
        listeners.add(listener);
    }

    @Override
    public UserManager getUserManager() throws RemoteException {
        return userManager;
    }

    @Override
    public synchronized boolean isUserExists(String username) throws RemoteException {
        for (int i = 0; i < userManager.getUsers().size(); i++) {
            if (Objects.equals(userManager.getUsers().get(i).getUsername(), username)){
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<Shape> loadCanvas()  throws RemoteException {
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
    public synchronized void newFile() throws RemoteException {
        storage.clearShape();
        for (IWhiteboardListener listener : listeners) {
            listener.updateCanvas();
        }
    }

    protected synchronized void notifyListeners(Rectangle rectangle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.rectDrawn(rectangle);
        }
    }

    protected synchronized void notifyListeners(TextItem text) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.textDrawn(text);
        }
    }

    protected synchronized void notifyListeners(Line line) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.lineDrawn(line);
        }
    }

    protected synchronized void notifyListeners(Triangle triangle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.triangleDrawn(triangle);
        }
    }
    protected synchronized void notifyListeners(Circle circle) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.circleDrawn(circle);
        }
    }
    protected synchronized void notifyListeners(Oval oval) throws RemoteException {
        for (IWhiteboardListener listener : listeners) {
            listener.ovalDrawn(oval);
        }
    }


    @Override
    public void drawRect(Rectangle rectangle) throws RemoteException {
        executorService.submit(() -> {

            System.out.println("Line drawn from (" + rectangle.getStartX() + ", " + rectangle.getStartY());
            try {
                notifyListeners(rectangle);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(rectangle);
        });
    }

    @Override
    public void drawCircle(Circle circle) throws RemoteException {
        executorService.submit(() -> {

            System.out.println("Circle drawn from (" + circle.getStartX() + ", " + circle.getStartY());
            try {
                notifyListeners(circle);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(circle);});
    }

    @Override
    public synchronized void drawTriangle(Triangle triangle) throws RemoteException {
        executorService.submit(() -> {

            System.out.println("triangle drawn from (" + triangle.getStartX() + ", " + triangle.getStartY());
            try {
                notifyListeners(triangle);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(triangle);});
    }

    @Override
    public synchronized void drawOval(Oval oval) throws RemoteException {
        executorService.submit(() -> {

            System.out.println("oval drawn from (" + oval.getStartX() + ", " + oval.getStartY());
            try {
                notifyListeners(oval);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(oval);});
    }

    @Override
    public synchronized void freeDraw(Line line) throws RemoteException {
        executorService.submit(() -> {

            System.out.println(line.getColor()+"Line drawn from (" + line.getStartX() + ", " + line.getStartY() + ") to (" + line.getEndX() + ", " + line.getEndY() + ")");
            try {
                notifyListeners(line);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(line);});
    }

    @Override
    public synchronized void drawText(TextItem text) throws RemoteException {
        executorService.submit(() -> {

            System.out.println(text.getText()+" add at" + text.getStartX() + ", " + text.getStartY() + "with" +text.getColor());
            try {
                notifyListeners(text);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            storage.addShape(text);});
    }
}
