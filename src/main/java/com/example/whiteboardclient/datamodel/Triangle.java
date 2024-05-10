package com.example.whiteboardclient.datamodel;

public class Triangle extends Shape {
    private double endX;
    private double endY;
    private double midX;

    public Triangle(String color, double startX, double startY, double endX, double endY, double midX) {
        super(color, startX, startY);
        this.endX = endX;
        this.endY = endY;
        this.midX = midX;
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

    public double getMidX() {
        return midX;
    }

    public void setMidX(double midX) {
        this.midX = midX;
    }
}
