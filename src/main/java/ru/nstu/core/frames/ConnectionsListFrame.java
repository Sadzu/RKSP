package ru.nstu.core.frames;

import javax.swing.*;

public class ConnectionsListFrame {
    private JFrame frame;
    private JTextArea textArea;


    public void init() {
        textArea = new JTextArea();
        frame = new JFrame("Connections List");

        textArea.setEditable(false);
        frame.add(textArea);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void update(String connectionList) {
        textArea.setText(connectionList);
    }
}
