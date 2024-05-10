package com.example.whiteboardclient;


import com.example.whiteboardclient.datamodel.*;

public interface WhiteboardUIUpdater {
     void displayLine(Line line);
     void displayTriangle(Triangle triangle);
     void displayCircle(Circle circle);
     void displayOval(Oval oval);
     void displayText(TextItem textItem);
     void displayRect(Rectangle rectangle);

}
