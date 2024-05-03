package com.example.whiteboardclient;

import javafx.application.Platform;
import org.example.IWhiteboardListener;
import org.example.Shape;

import java.rmi.RemoteException;

public class WhiteboardListener implements IWhiteboardListener {
    @Override
    public void shapeDrawn(Shape shape) throws RemoteException {
        // 更新客户端的UI以显示新绘制的形状
        Platform.runLater(() -> {
            // 假设你有一个方法来处理形状的绘制
            //drawShape(shape);
        });
    }
}
