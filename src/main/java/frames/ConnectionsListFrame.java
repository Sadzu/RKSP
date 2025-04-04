package frames;

import javax.swing.*;

public class ConnectionsListFrame {
    JFrame frame;
    JTextArea textArea;


    public void init() {
        frame = new JFrame("Connections List");
        textArea = new JTextArea();
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
