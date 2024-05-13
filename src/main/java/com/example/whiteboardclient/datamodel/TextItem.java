package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class TextItem extends Shape implements Serializable {
    private String text;
    private String fontName;
    private double fontSize;

    public TextItem(String text, double x, double y, double fontSize, String color) {
        super(color,x,y,"text");
        this.text = text;
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

// 构造函数、getter和setter省略
}
