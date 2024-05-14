package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.*;
import com.example.whiteboardclient.listener.WhiteboardListener;
import com.example.whiteboardclient.WhiteboardUIUpdater;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;


public class WhiteBoardController implements  Serializable, WhiteboardUIUpdater {
    @FXML public Button ovelButton;
    @FXML public ColorPicker colorPicker;
    @FXML
    public Button eraser;
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

    private Color selectedColor=Color.BLACK;



    public void initialize() throws RemoteException {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/WhiteboardServer";
            // RMI 服务器查找
            server = (IWhiteboard) Naming.lookup(remoteObjectName);
            if (!server.isUserExists(WhiteBoardApplication.getUsername())) {
                System.err.println("User does not exist: " + WhiteBoardApplication.getUsername());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Disconnect ");
                alert.show();
                return;
            }
            WhiteboardListener listener = new WhiteboardListener(this);
            server.addWhiteboardListener(listener);
        } catch (Exception e) {
            System.err.println("RMI server connection error: " + e.getMessage());
            e.printStackTrace();
            // 显示错误弹出窗口
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            // Optional: 关闭主窗口
            System.exit(1);
        }


        gc = canvas.getGraphicsContext2D();
        prepareCanvas(gc);
        canvas.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 捕捉初始状态
        });
        updateCanvas(server);
    }
    private void prepareCanvas(GraphicsContext gc) {
        gc.setStroke(Color.BLACK); // 设置画笔颜色
        gc.setLineWidth(2); // 设置画笔宽度
    }
    public void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 设置填充颜色为白色（或其他任何你希望的颜色）
        gc.setFill(Color.WHITE);

        // 填充整个画布
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void updateCanvas(IWhiteboard server) throws RemoteException {
        clearCanvas(canvas);
        List<Shape> origin =server.loadCanvas();
        for (int i = 0; i < origin.size(); i++) {
            if (origin.get(i).getType().equals("triangle")){
                displayTriangle((Triangle) origin.get(i));
            } else if (origin.get(i).getType().equals("rectangle")) {
                displayRect((Rectangle) origin.get(i));
            }else if (origin.get(i).getType().equals("line")){
                displayLine((Line) origin.get(i));
            }else if (origin.get(i).getType().equals("circle")){
                displayCircle((Circle) origin.get(i));
            } else if (origin.get(i).getType().equals("oval")) {
                displayOval((Oval) origin.get(i));
            } else if (origin.get(i).getType().equals("text")) {
                displayText((TextItem) origin.get(i));
            }
        }
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
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

        canvas.setOnMouseDragged(event -> {
            double endX = event.getX();
            double endY = event.getY();
            gc.strokeLine(startX, startY, endX, endY); // 绘制线条到 Canvas
            try {
                // 发送绘制数据到服务器
                server.freeDraw(new Line(selectedColor.toString(),startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
            startX = endX;
            startY = endY;
        });

    }
    public void onEraser(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(Color.WHITE); // 设置画笔颜色为背景颜色

        canvas.setOnMouseDragged(event -> {
            double endX = event.getX();
            double endY = event.getY();
            gc.strokeLine(startX, startY, endX, endY); // 绘制线条到 Canvas
            try {
                // 发送绘制数据到服务器
                server.freeDraw(new Line(Color.WHITE.toString(),startX, startY, endX, endY ));
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
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

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
                server.drawRect(new Rectangle(selectedColor.toString(),startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawCircle(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

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
            double endX = event.getX();
            double endY = event.getY();
            try {
                // 发送绘制数据到服务器
                server.drawCircle(new Circle(selectedColor.toString(),startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawOval(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double radiusX = Math.abs(endX - startX) / 2;
            double radiusY = Math.abs(endY - startY) / 2;
            double centerX = (startX + endX) / 2; // 改为计算 startX 和 endX 的平均值
            double centerY = (startY + endY) / 2; // 改为计算 startY 和 endY 的平均值

            gc.strokeOval(centerX - radiusX, centerY - radiusY, 2 * radiusX, 2 * radiusY);
        });

        canvas.setOnMouseReleased(event -> {
            // 可以在这里发送最终形状数据到服务器
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // 更新快照
            double endX = event.getX();
            double endY = event.getY();
            try {
                // 发送绘制数据到服务器
                server.drawOval(new Oval(selectedColor.toString(),startX, startY, endX, endY ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    public void onDrawTriangle(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

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
            double endX = event.getX();
            double endY = event.getY();
            double midX = (startX + endX) / 2;

            try {
                // 发送绘制数据到服务器
                server.drawTriangle(new Triangle(selectedColor.toString(),startX, startY, endX, endY,midX ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawText(ActionEvent actionEvent) {
        gc.setStroke(selectedColor); // 设置画笔颜色为背景颜色

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

    @Override
    public void displayTriangle(Triangle triangle) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(triangle.getColor()));
            double endX = triangle.getEndX();
            double endY = triangle.getEndY();
            double midX = triangle.getMidX();
            double[] xPoints = {triangle.getStartX(), endX, midX};
            double[] yPoints = {endY, endY, triangle.getStartY()};
            gc.strokePolygon(xPoints, yPoints, 3);
        });
    }

    @Override
    public void displayCircle(Circle circle) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(circle.getColor()));
            double endX = circle.getEndX();
            double endY = circle.getEndY();
            double radius = Math.sqrt(Math.pow(endX - circle.getStartX(), 2) + Math.pow(endY - circle.getStartY(), 2));
            gc.strokeOval(circle.getStartX() - radius, circle.getStartY() - radius, 2 * radius, 2 * radius);
        });
    }

    @Override
    public void displayOval(Oval oval) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(oval.getColor()));
            double endX = oval.getEndX();
            double endY = oval.getEndY();
            double radiusX = Math.abs(endX -  oval.getStartX()) / 2;
            double radiusY = Math.abs(endY - oval.getStartY()) / 2;
            double centerX = (oval.getStartX() + endX) / 2;
            double centerY = (oval.getStartY() + endY)/2;

            gc.strokeOval(centerX - radiusX, centerY - radiusY, 2 * radiusX, 2 * radiusY);

            gc.strokeOval(centerX - radiusX, centerY - radiusY, 2 * radiusX, 2 * radiusY);
        });
    }

    @Override
    public void displayText(TextItem textItem) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(textItem.getColor()));
            gc.strokeText(textItem.getText(),textItem.getStartX(),textItem.getStartY());
        });
    }

    @Override
    public void displayRect(Rectangle rectangle) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(rectangle.getColor()));

            double endX = rectangle.getEndX();
            double endY = rectangle.getEndY();
            double width = Math.abs(endX - rectangle.getStartX());
            double height = Math.abs(endY - rectangle.getStartY());
            gc.strokeRect(Math.min(rectangle.getStartX(), endX), Math.min(rectangle.getStartY(), endY), width, height);
        });
    }

    @Override
    public void updateCanvas() throws RemoteException{
        updateCanvas(server);
    }

    public void onColorSelected(ActionEvent actionEvent) {
        selectedColor = colorPicker.getValue();
        gc.setStroke(selectedColor); // 设置画笔颜色
    }

    @FXML
    private void onCloseWindow(WindowEvent event) {
        try {
            // 关闭RMI连接
            if (server != null) {
            }
        } catch (Exception e) {
            System.err.println("Error closing RMI connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
