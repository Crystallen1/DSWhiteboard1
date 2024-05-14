package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class Line extends Shape implements Serializable {
    private double endX;
    private double endY;

    // 构造方法
    public Line(String color, double startX, double startY, double endX, double endY,int size) {
        super(color, startX, startY,"line",size);
        this.endX = endX;
        this.endY = endY;
    }

    // 绘制直线

    // 获取终点X坐标
    public double getEndX() {
        return endX;
    }

    // 获取终点Y坐标
    public double getEndY() {
        return endY;
    }

    // 设置终点X坐标
    public void setEndX(int endX) {
        this.endX = endX;
    }

    // 设置终点Y坐标
    public void setEndY(int endY) {
        this.endY = endY;
    }
}
