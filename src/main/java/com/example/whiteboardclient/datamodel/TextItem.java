package com.example.whiteboardclient.datamodel;

import java.io.Serializable;

public class TextItem extends Shape implements Serializable {
    private String text;
    private String fontName;

    public TextItem(String text, double x, double y, String color,int size) {
        super(color,x,y,"text",size);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

// 构造函数、getter和setter省略
}
