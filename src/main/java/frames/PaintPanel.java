package frames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import objects.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PaintPanel extends JPanel {
    private final ArrayList<Rectangle> _rectangles;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PaintPanel() {
        _rectangles = new ArrayList<>();
    }

    public static void drawRect(Rectangle rectangle, Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Rectangle rectangle : _rectangles) {
            drawRect(rectangle, g);
        }
    }

    public void addRandomRectFunc() {
        Rectangle rectangle = new Rectangle(
                Math.round(Math.random()*900),
                Math.round(Math.random()*700),
                Math.round(Math.random()*100),
                Math.round(Math.random()*100));
        _rectangles.add(rectangle);
    }

    public String[] serializeToJson() {
        String[] jsons = new String[_rectangles.size()];
        for (int i = 0; i < _rectangles.size(); i++) {
            jsons[i] = gson.toJson(_rectangles.get(i));
        }
        return jsons;
    }

    public void deserializeFromJson(String json) {
        _rectangles.add(gson.fromJson(json, Rectangle.class));
    }
}
