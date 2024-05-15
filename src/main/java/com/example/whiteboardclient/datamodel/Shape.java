package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public abstract class Shape implements Serializable {
    protected String color;
    protected double startX;
    protected double startY;
    protected String type;

    protected int size;

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    // 构造方法
    public Shape(String color, double startX, double startY,String type,int size) {
        this.color = color;
        this.startX = startX;
        this.startY = startY;
        this.type = type;
        this.size=size;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public double getStartX() {
        return startX;
    }
    public double getStartY() {
        return startY;
    }
}
