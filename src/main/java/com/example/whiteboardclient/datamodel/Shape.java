package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public abstract class Shape implements Serializable {
    protected String color;
    protected double startX;
    protected double startY;
    protected String type;

    public String getType() {
        return type;
    }

    // 构造方法
    public Shape(String color, double startX, double startY,String type) {
        this.color = color;
        this.startX = startX;
        this.startY = startY;
        this.type = type;
    }

    // 获取颜色
    public String getColor() {
        return color;
    }

    // 设置颜色
    public void setColor(String color) {
        this.color = color;
    }

    // 绘制形状的抽象方法

    // 获取起始X坐标
    public double getStartX() {
        return startX;
    }

    // 获取起始Y坐标
    public double getStartY() {
        return startY;
    }
}