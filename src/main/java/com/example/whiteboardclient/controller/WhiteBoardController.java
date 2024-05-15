package com.example.whiteboardclient.controller;

import com.example.whiteboardclient.WhiteBoardApplication;
import com.example.whiteboardclient.connect.IWhiteboard;
import com.example.whiteboardclient.datamodel.*;
import com.example.whiteboardclient.listener.WhiteboardListener;
import com.example.whiteboardclient.UIUpdater.WhiteboardUIUpdater;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Queue;


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
    private IWhiteboard server; // RMI interface
    private double startX, startY;

    private GraphicsContext gc;
    private WritableImage snapshot;

    private Color selectedColor=Color.BLACK;



    public void initialize() throws RemoteException {
        try {
            String remoteObjectName = "//"+WhiteBoardApplication.getServerIPAddress()+":"+WhiteBoardApplication.getServerPort()+"/WhiteboardServer";
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to RMI server: " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        colorPicker.setValue(Color.BLACK);
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
        gc.setStroke(Color.BLACK); // set color
        gc.setLineWidth(2); // set size
    }
    public void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void updateCanvas(IWhiteboard server) throws RemoteException {
        clearCanvas(canvas);
        Queue<Shape> origin =server.loadCanvas();
        for (Shape shape : origin) {
            String type = shape.getType();
            switch (type) {
                case "triangle":
                    displayTriangle((Triangle) shape);
                    break;
                case "rectangle":
                    displayRect((Rectangle) shape);
                    break;
                case "line":
                    displayLine((Line) shape);
                    break;
                case "circle":
                    displayCircle((Circle) shape);
                    break;
                case "oval":
                    displayOval((Oval) shape);
                    break;
                case "text":
                    displayText((TextItem) shape);
                    break;
            }
        }
    }

    private void clearEventHandlers() {
        // Clear all event handlers
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseClicked(null);
    }

    private void restoreCanvas() {
        gc.drawImage(snapshot, 0, 0);
    }

    public void onFreeDraw(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setLineWidth(5);
        gc.setStroke(selectedColor);

        canvas.setOnMouseDragged(event -> {
            double endX = event.getX();
            double endY = event.getY();
            // Draw a line on the canvas from the start position to the current mouse position
            gc.strokeLine(startX, startY, endX, endY);
            try {
                // Send the drawing data to the server for synchronization with other clients
                server.freeDraw(new Line(selectedColor.toString(),startX, startY, endX, endY,5));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }

            // Update the start position to the current mouse position for continuous drawing
            startX = endX;
            startY = endY;
        });
    }
    public void onEraser(ActionEvent actionEvent) {
        // Create a new dialog to select the eraser size
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("choose the eraser size");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));

        Label label = new Label("choose the eraser size：");
        ComboBox<Integer> sizeComboBox = new ComboBox<>();
        // Add eraser size options to the ComboBox
        sizeComboBox.getItems().addAll(5, 10, 15, 20, 25);

        vbox.getChildren().addAll(label, sizeComboBox);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Set a result converter to handle the dialog result
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                gc.setLineWidth(sizeComboBox.getValue());
                return sizeComboBox.getValue();
            }
            return null;
        });
        dialog.show();

        clearEventHandlers();
        gc.setStroke(Color.WHITE);

        canvas.setOnMouseDragged(event -> {
            double endX = event.getX();
            double endY = event.getY();
            gc.strokeLine(startX, startY, endX, endY);
            try {
                server.freeDraw(new Line(Color.WHITE.toString(),startX, startY, endX, endY,sizeComboBox.getValue()));
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
        gc.setStroke(selectedColor);
        gc.setLineWidth(5);

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
            double endX = event.getX();
            double endY = event.getY();
            try {
                server.drawRect(new Rectangle(selectedColor.toString(),startX, startY, endX, endY,5 ));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawCircle(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor);
        gc.setLineWidth(5);

        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            gc.strokeOval(startX - radius, startY - radius, 2 * radius, 2 * radius);
        });

        canvas.setOnMouseReleased(event -> {
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // update snapshot
            double endX = event.getX();
            double endY = event.getY();
            try {
                server.drawCircle(new Circle(selectedColor.toString(),startX, startY, endX, endY,5));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawOval(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor);
        gc.setLineWidth(5);

        canvas.setOnMouseDragged(event -> {
            restoreCanvas();

            double endX = event.getX();
            double endY = event.getY();
            double radiusX = Math.abs(endX - startX) / 2;
            double radiusY = Math.abs(endY - startY) / 2;
            double centerX = (startX + endX) / 2;
            double centerY = (startY + endY) / 2;

            gc.strokeOval(centerX - radiusX, centerY - radiusY, 2 * radiusX, 2 * radiusY);
        });

        canvas.setOnMouseReleased(event -> {
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // update snapshot
            double endX = event.getX();
            double endY = event.getY();
            try {
                server.drawOval(new Oval(selectedColor.toString(),startX, startY, endX, endY,5));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    public void onDrawTriangle(ActionEvent actionEvent) {
        clearEventHandlers();
        gc.setStroke(selectedColor);
        gc.setLineWidth(5);

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
            snapshot = canvas.snapshot(new SnapshotParameters(), null);  // update snapshot
            double endX = event.getX();
            double endY = event.getY();
            double midX = (startX + endX) / 2;

            try {
                server.drawTriangle(new Triangle(selectedColor.toString(),startX, startY, endX, endY,midX,5));
                //server.drawLine(new Line(startX, startY, endX, endY, "black"));
            } catch (Exception e) {
                System.err.println("Error sending line to server: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void onDrawText(ActionEvent actionEvent) {
        gc.setStroke(selectedColor);
        gc.setLineWidth(1);

        canvas.setOnMouseClicked(event -> {
            TextInputDialog textInputDialog = new TextInputDialog("Text here");
            textInputDialog.setTitle("Input Text");
            textInputDialog.setHeaderText("Enter text to draw:");
            textInputDialog.showAndWait().ifPresent(text -> {
                gc.fillText(text, event.getX(), event.getY());
                try {
                    server.drawText(new TextItem(text,startX, startY, "black",1));
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
            gc.setLineWidth(line.getSize());
            gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        });
    }

    @Override
    public void displayTriangle(Triangle triangle) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(triangle.getColor()));
            gc.setLineWidth(triangle.getSize());
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
            gc.setLineWidth(circle.getSize());
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
            gc.setLineWidth(oval.getSize());
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
            gc.setLineWidth(textItem.getSize());
            gc.strokeText(textItem.getText(),textItem.getStartX(),textItem.getStartY());
        });
    }

    @Override
    public void displayRect(Rectangle rectangle) {
        Platform.runLater(() -> {
            gc.setStroke(Color.web(rectangle.getColor()));
            gc.setLineWidth(rectangle.getSize());

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
        gc.setStroke(selectedColor);
    }

    @FXML
    private void onCloseWindow(WindowEvent event) {
        try {
            if (server != null) {
            }
        } catch (Exception e) {
            System.err.println("Error closing RMI connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
