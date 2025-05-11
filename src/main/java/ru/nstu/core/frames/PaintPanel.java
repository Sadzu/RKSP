package ru.nstu.core.frames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.nstu.core.objects.Circle;
import ru.nstu.core.objects.GraphicObject;
import ru.nstu.core.objects.Rectangle;
import ru.nstu.core.objects.dto.ObjectDto;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaintPanel extends JPanel {
    private final ArrayList<GraphicObject> _graphicObjects;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PaintPanel() {
        _graphicObjects = new ArrayList<>();
    }

    public static void drawRect(Rectangle rectangle, Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    public static void drawCircle(Circle circle, Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillOval((int) circle.getX(), (int) circle.getY(), (int) circle.getRadius(), (int) circle.getRadius());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (GraphicObject graphicObject : _graphicObjects) {
            if (graphicObject.getType().equals("Rectangle")) {
                drawRect((ru.nstu.core.objects.Rectangle) graphicObject, g);
            } else if (graphicObject.getType().equals("Circle")) {
                drawCircle((Circle) graphicObject, g);
            }
        }
    }

    public void addRandomRectFunc() {
        Rectangle rectangle = new Rectangle(
                Math.round(Math.random()*900),
                Math.round(Math.random()*700),
                Math.round(Math.random()*100),
                Math.round(Math.random()*100));
        _graphicObjects.add(rectangle);
    }

    public void addRandomCircleFunc() {
        Circle circle = new Circle(
                Math.round(Math.random()*900),
                Math.round(Math.random()*700),
                Math.round(Math.random()*100)
        );
        _graphicObjects.add(circle);
    }

    public String[] serializeToJson() {
        String[] jsons = new String[_graphicObjects.size()];
        for (int i = 0; i < _graphicObjects.size(); i++) {
            jsons[i] = gson.toJson(_graphicObjects.get(i));
        }
        return jsons;
    }

    public void deserializeFromJson(String json) {
        List<ObjectDto> objects = gson.fromJson(json, new TypeToken<List<ObjectDto>>(){}.getType());
        for (ObjectDto graphicObject : objects) {
            if (graphicObject.getType().equals("Rectangle")) {
                _graphicObjects.add(new Rectangle(graphicObject.getX(), graphicObject.getY(), graphicObject.getWidth(), graphicObject.getHeight()));
            } else {
                _graphicObjects.add(new Circle(graphicObject.getX(), graphicObject.getY(), graphicObject.getRadius()));
            }
        }

        repaint();
    }

    public int getObjectsCount() {
        return _graphicObjects.size();
    }

    public ArrayList<GraphicObject> getObjects() {
        return _graphicObjects;
    }

    public String serializeSelectedToJson(SelectItemsFrame selectItemsFrame) {
        ArrayList<JCheckBox> checkBoxes = selectItemsFrame.getItemsSelectors();
        ArrayList<GraphicObject> objects = new ArrayList<>();
        for (int i = 0; i < _graphicObjects.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                objects.add(_graphicObjects.get(i));
            }
        }

        return gson.toJson(objects);
    }
}
