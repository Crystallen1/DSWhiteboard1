package com.example.whiteboardclient;

import com.example.whiteboardclient.datamodel.Shape;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasStorage implements Serializable{
    private List<Shape> shapes = new ArrayList<>();
    private String filePath = "canvas.dat";

    public CanvasStorage() {

    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        //saveShapes();
    }
    public void clearShape(){
        shapes.clear();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void saveShapes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(shapes);
        } catch (IOException e) {
            System.err.println("Error saving shapes: " + e.getMessage());
        }
    }
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
                shapes = (List<Shape>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading shapes: " + e.getMessage());
            }
        }
    }
    @SuppressWarnings("unchecked")
    public void loadShapesFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
                shapes = (List<Shape>) in.readObject();
                System.out.println(shapes.size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading shapes: " + e.getMessage());
            }
        }
    }
}
