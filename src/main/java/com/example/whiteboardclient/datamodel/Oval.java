package com.example.whiteboardclient.datamodel;

public class Oval extends Shape {
    private double endX;
    private double endY;

    public Oval(String color, double startX, double startY, double endX, double endY) {
        super(color, startX, startY,"oval");
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
