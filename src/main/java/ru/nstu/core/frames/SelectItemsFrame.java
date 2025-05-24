
package ru.nstu.core.frames;

import lombok.Getter;
import ru.nstu.core.objects.GraphicObject;

import javax.swing.*;
import java.util.ArrayList;

public class SelectItemsFrame {
    private JFrame frame;
    @Getter
    private  ArrayList<JCheckBox> itemsSelectors;
    private  JPanel panel;

    public void show(ArrayList<GraphicObject> items) {

        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public  void init() {
        frame = new JFrame();
        frame.setTitle("Select Items");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setBounds(0, 0, 800, 600);
        itemsSelectors = new ArrayList<>();
        panel = new JPanel();
        for (JCheckBox checkBox : itemsSelectors) {
            panel.add(checkBox);
        }
        frame.add(panel);
    }

    public void update(ArrayList<GraphicObject> items) {
        itemsSelectors.clear();
        for (GraphicObject object : items) {
            itemsSelectors.add(new JCheckBox(object.toString()));
        }
        panel.removeAll();
        for (JCheckBox checkBox : itemsSelectors) {
            panel.add(checkBox);
        }
    }
}
