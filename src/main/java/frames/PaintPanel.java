package frames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import objects.Circle;
import objects.GraphicObject;
import objects.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
                drawRect((Rectangle) graphicObject, g);
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
        if (json.contains("Circle")) {
            _graphicObjects.add(gson.fromJson(json, Circle.class));
        } else if (json.contains("Rectangle")) {
            _graphicObjects.add(gson.fromJson(json, Rectangle.class));
        }
    }
}
