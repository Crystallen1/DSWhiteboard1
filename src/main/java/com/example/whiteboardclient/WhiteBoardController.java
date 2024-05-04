package com.example.whiteboardclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.example.*;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class WhiteBoardController implements  Serializable,UIUpdater {
    @FXML
    private Canvas canvas;
    @FXML private Button freeDrawButton;
    @FXML private Button rectangleButton;
    @FXML private Button circleButton;
    @FXML private Button triangleButton;
    @FXML private Button textButton;
    private IWhiteboard server; // RMI 服务接口
    private double startX, startY; // 绘图起始点

    private GraphicsContext gc;
    private WritableImage snapshot;



    public void initialize() {
        try {
            String remoteObjectName = "//localhost:20017/WhiteboardServer";
            // RMI 服务器查找
            server = (IWhiteboard) Naming.lookup(remoteObjectName);
            WhiteboardListener listener = new WhiteboardListener(this);
            server.addWhiteboardListener(listener);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
        }

        gc = canvas.getGraphicsContext2D();
        prepareCanvas(gc);
        canvas.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 捕捉初始状态
        });
    }
    private void prepareCanvas(GraphicsContext gc) {
        gc.setStroke(Color.BLACK); // 设置画笔颜色
        gc.setLineWidth(2); // 设置画笔宽度
    }

    private void clearEventHandlers() {
        // 清除所有事件处理
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseClicked(null);
    }

    private void restoreCanvas() {
        gc.drawImage(snapshot, 0, 0);  // 恢复到捕捉的初始状态
    }

    public void onFreeDraw(ActionEvent actionEvent) {
        clearEventHandlers();

        canvas.setOnMouseDragged(event -> {
            double endX = event.getX();
            double endY = event.getY();
            gc.strokeLine(startX, startY, endX, endY); // 绘制线条到 Canvas
            try {
                // 发送绘制数据到服务器
                server.freeDraw(new Line("black",startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
            startX = endX;
            startY = endY;
        });

    }

    public void onDrawRectangle(ActionEvent actionEvent) {
        clearEventHandlers();
        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            gc.strokeRect(Math.min(startX, endX), Math.min(startY, endY), width, height);
        });

        canvas.setOnMouseReleased(event -> {
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 更新快照
            // 可以在这里发送最终形状数据到服务器
            double endX = event.getX();
            double endY = event.getY();
            try {
                // 发送绘制数据到服务器
                server.drawShape(new Rectangle("black",startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawCircle(ActionEvent actionEvent) {
        clearEventHandlers();

        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            gc.strokeOval(startX - radius, startY - radius, 2 * radius, 2 * radius);
        });

        canvas.setOnMouseReleased(event -> {
            // 可以在这里发送最终形状数据到服务器
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 更新快照
        });
    }

    public void onDrawTriangle(ActionEvent actionEvent) {
        clearEventHandlers();

        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double midX = (startX + endX) / 2;
            double[] xPoints = {startX, endX, midX};
            double[] yPoints = {endY, endY, startY};
            gc.strokePolygon(xPoints, yPoints, 3);
        });
        canvas.setOnMouseReleased(event -> {
            // 可以在这里发送最终形状数据到服务器
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 更新快照
        });
    }

    public void onDrawText(ActionEvent actionEvent) {
        canvas.setOnMouseClicked(event -> {
            TextInputDialog textInputDialog = new TextInputDialog("Text here");
            textInputDialog.setTitle("Input Text");
            textInputDialog.setHeaderText("Enter text to draw:");
            textInputDialog.showAndWait().ifPresent(text -> {
                gc.fillText(text, event.getX(), event.getY());
                try {
                    // 发送绘制数据到服务器
                    server.drawText(new TextItem(text,startX, startY, 1, "black" ));
                    //server.drawLine(new Line(startX, startY, endX, endY, "black"));
                } catch (Exception e) {
                    System.err.println("Error sending line to server: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        });

    }


    @Override
    public void displayLine(Line line) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(line.getColor()));
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        });
    }
}
