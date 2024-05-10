package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class TextItem implements Serializable {
    private String text;
    private double x, y; // Position
    private String fontName;
    private double fontSize;
    private String color;

    public TextItem(String text, double x, double y, double fontSize, String color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
// 构造函数、getter和setter省略
}
