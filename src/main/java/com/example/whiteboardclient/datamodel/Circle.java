package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class Circle extends Shape implements Serializable {
    private double endX;
    private double endY;

    public Circle(String color, double startX, double startY, double endX, double endY) {
        super(color, startX, startY);
        this.endX = endX;
        this.endY = endY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}
