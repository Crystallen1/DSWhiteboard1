package com.example.whiteboardclient.datamodel;

import com.example.whiteboardclient.datamodel.Shape;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class CanvasStorage implements Serializable{
    private Queue<Shape> shapes = new ConcurrentLinkedQueue<>();
    private String filePath = "canvas.dat";

    public CanvasStorage() {

    }
    /**
     * Adds a shape to the storage.
     *
     * @param shape The Shape object to be added.
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
        //saveShapes();
    }
    /**
     * Clears all shapes from the storage.
     */
    public void clearShape(){
        shapes.clear();
    }
    /**
     * Retrieves all shapes from the storage.
     *
     * @return A queue of Shape objects currently stored.
     */
    public Queue<Shape> getShapes() {
        return shapes;
    }
    /**
     * Saves the current shapes to the default file path.
     */
    public void saveShapes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(shapes);
        } catch (IOException e) {
            System.err.println("Error saving shapes: " + e.getMessage());
        }
    }
    /**
     * Saves the current shapes to a specified file path.
     *
     * @param filePath The file path where the shapes should be saved.
     */
    public void saveAsShapes(String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(shapes);
        } catch (IOException e) {
            System.err.println("Error saving shapes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadShapes() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
                shapes = (Queue<Shape>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading shapes: " + e.getMessage());
            }
        }
    }
    /**
     * Loads shapes from a specified file path.
     *
     * @param filePath The file path from which the shapes should be loaded.
     */
    @SuppressWarnings("unchecked")
    public void loadShapesFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
                shapes = (Queue<Shape>) in.readObject();
                System.out.println(shapes.size());
            } catch (IOException | ClassNotFoundException e) {
                Platform.runLater(() -> {

                    Alert alert = new Alert(Alert.AlertType.ERROR,"Error File");
                alert.showAndWait();});
                System.err.println("Error loading shapes: " + e.getMessage());
            }
        }
    }
}
