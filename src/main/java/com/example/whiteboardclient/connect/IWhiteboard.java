package com.example.whiteboardclient.connect;

import com.example.whiteboardclient.datamodel.*;
import com.example.whiteboardclient.listener.IWhiteboardListener;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWhiteboard extends Remote, Serializable {
    void drawRect(Rectangle rectangle) throws RemoteException;

    void drawCircle(Circle circle) throws RemoteException;

    void drawTriangle(Triangle triangle) throws RemoteException;

    void drawOval(Oval oval) throws RemoteException;

    // 自由绘制，画笔功能
    void freeDraw(Line line) throws RemoteException;

    // 输入文字
    void drawText(TextItem text) throws RemoteException;

    // 清除白板
    void clear() throws RemoteException;

    void addWhiteboardListener(IWhiteboardListener listener) throws RemoteException;

    UserManager getUserManager()throws RemoteException;

    boolean isUserExists(String username)throws RemoteException;

}
