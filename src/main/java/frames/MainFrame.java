package frames;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import events.ConnectionsReceivedEvent;
import events.ObjectReceivedEvent;
import server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame {
    private JFrame _frame;
    private PaintPanel _panel;
    private JPanel _buttonPanel;

    private ConnectionsListFrame connectionsListFrame;

    private JButton _sendButton;
    private JButton _addRandomRectButton;
    private JButton _stopConnectionButton;
    private JButton _addRandomCircleButton;
    private JButton _showConnectionsButton;

    private JTextArea _sendToIdTextField;

    private Timer connectionsUpdateTimer;

    private Client client;

    private EventBus eventBus;

    public void init() throws IOException {
        eventBus = new EventBus();
        eventBus.register(this);
        client = new Client();
        int id = client.init(eventBus);

        _frame = new JFrame("Client: " + id);
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.setSize(1000, 1000);
        _frame.setBounds(0, 0, 1000, 800);

        _panel = new PaintPanel();

        _makeAddRandomRectButton();
        _makeSendButton();
        _makeStopConnectionButton();
        _makeAddRandomCircleButton();
        _makeShowConnectionsButton();
        _makeSendToIdTextField();

        _frame.add(_panel);

        _buttonPanel = new JPanel();
        _buttonPanel.add(_addRandomRectButton);
        _buttonPanel.add(_sendButton);
        _buttonPanel.add(_stopConnectionButton);
        _buttonPanel.add(_addRandomCircleButton);
        _buttonPanel.add(_showConnectionsButton);
        _buttonPanel.add(_sendToIdTextField);

        _frame.getContentPane().add(_buttonPanel, BorderLayout.SOUTH);

        connectionsListFrame = new ConnectionsListFrame();
        connectionsListFrame.init();
        //connectionsListFrame.update(getConnectionsList());
        connectionsUpdateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.send("gimmeConnections");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        connectionsUpdateTimer.start();

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
                try {
                    _sendTo(Integer.parseInt(_sendToIdTextField.getText()), _panel.getObjectsCount());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void _makeStopConnectionButton() {
        _stopConnectionButton = new JButton("Stop connection");
        _stopConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
    }

    public void paintObjectFromSocket(String json) {
        _panel.deserializeFromJson(json);
        _panel.repaint();
    }

    private void _makeAddRandomCircleButton() {
        _addRandomCircleButton = new JButton("Add Random Circle");
        _addRandomCircleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _panel.addRandomCircleFunc();
                _panel.repaint();
            }
        });
    }

    private void _makeShowConnectionsButton() {
        _showConnectionsButton = new JButton("Show Connections");
        _showConnectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionsListFrame.show();
            }
        });
    }

    private void _makeSendToIdTextField() {
        _sendToIdTextField = new JTextArea();
        _sendToIdTextField.setSize(30, 10);
        _sendToIdTextField.setEditable(true);
    }

    private void _sendTo(int id, int count) {
        try {
            client.send("send " + id + " " + count);
            for (String json : _panel.serializeToJson()) {
                client.send(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onObjectReceived(ObjectReceivedEvent objectReceivedEvent) {
        _panel.deserializeFromJson(objectReceivedEvent.getObject());
        _panel.repaint();
    }

    @Subscribe
    public void onConnectionsReceived(ConnectionsReceivedEvent connectionsReceivedEvent) {
        connectionsListFrame.update(connectionsReceivedEvent.getConnections());
    }

    private void exit() {
        try {
            connectionsUpdateTimer.stop();
            client.close();
            _frame.setVisible(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
