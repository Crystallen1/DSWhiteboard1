package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class Line extends Shape implements Serializable {
    private double endX;
    private double endY;
    public Line(String color, double startX, double startY, double endX, double endY,int size) {
        super(color, startX, startY,"line",size);
        this.endX = endX;
        this.endY = endY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }
}
