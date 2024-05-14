package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class Rectangle extends Shape implements Serializable {
    private double endX;
    private double endY;

    public Rectangle(String color, double startX, double startY, double endX, double endY,int size) {
        super(color, startX, startY,"rectangle",size);
        this.endX = endX;
        this.endY = endY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

}
