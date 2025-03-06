package frames;

import server.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame {
    private JFrame _frame;
    private PaintPanel _panel;
    private JPanel _buttonPanel;

    private JButton _sendButton;
    private JButton _addRandomRectButton;
    private JButton _stopConnectionButton;

    private Connection _connection;

    public void init(boolean isServer) throws IOException {
        _connection = new Connection(isServer);
        _connection.connect("localhost", 6666, this);

        System.out.println("Connected to server");

        _frame = new JFrame();
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(1000, 1000);
        _frame.setBounds(100, 100, 1000, 800);

        _panel = new PaintPanel();

        _makeAddRandomRectButton();
        _makeSendButton();
        _makeStopConnectionButton();

        _frame.add(_panel);

        _buttonPanel = new JPanel();
        _buttonPanel.add(_addRandomRectButton);
        _buttonPanel.add(_sendButton);
        _buttonPanel.add(_stopConnectionButton);

        _frame.getContentPane().add(_buttonPanel, BorderLayout.SOUTH);
        _frame.setVisible(true);
    }

    private void _makeAddRandomRectButton() {
        _addRandomRectButton = new JButton("Add Random Rect");
        _addRandomRectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _panel.addRandomRectFunc();
                _panel.repaint();
            }
        });
    }

    private void _makeSendButton() {
        _sendButton = new JButton("Send");
        _sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _send();
            }
        });
    }

    private void _send() {
        _connection.sendJsons(_panel.serializeToJson());
    }

    private void _makeStopConnectionButton() {
        _stopConnectionButton = new JButton("Stop connection");
        _stopConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _connection.send("exit");
                _frame.setVisible(false);
            }
        });
    }

    public void paintRectsFromSocket(String json) {
        _panel.deserializeFromJson(json);
        _panel.repaint();
    }
}
