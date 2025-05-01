package frames;

import lombok.Getter;
import objects.GraphicObject;

import javax.swing.*;
import java.util.ArrayList;

public class SelectItemsFrame {
    private JFrame frame;
    @Getter
    private  ArrayList<JCheckBox> itemsSelectors;
    private  JPanel panel;

    public void show(ArrayList<GraphicObject> items) {
        update(items);

        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public  void init(ArrayList<GraphicObject> items) {
        frame = new JFrame();
        frame.setTitle("Select Items");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setBounds(0, 0, 800, 600);
        itemsSelectors = new ArrayList<>();
        panel = new JPanel();
        for (GraphicObject object : items) {
            itemsSelectors.add(new JCheckBox(object.toString()));
        }
        for (JCheckBox checkBox : itemsSelectors) {
            panel.add(checkBox);
        }
        frame.add(panel);
    }

    private void update(ArrayList<GraphicObject> items) {
        itemsSelectors.clear();
        for (GraphicObject object : items) {
            itemsSelectors.add(new JCheckBox(object.toString()));
        }
        for (JCheckBox checkBox : itemsSelectors) {
            panel.add(checkBox);
        }
    }
}
