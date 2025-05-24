package ru.nstu.core.frames;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import ru.nstu.net.RestClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame {
    Integer id;

    private JFrame _frame;
    private PaintPanel _panel;
    private JPanel _buttonPanel;

    private ConnectionsListFrame connectionsListFrame;

    private JButton _sendButton;
    private JButton _addRandomRectButton;
    private JButton _stopConnectionButton;
    private JButton _addRandomCircleButton;
    private JButton _showConnectionsButton;
    private JButton _selectItemsToSendButton;
    private JButton _launchNewClientButton;

    private JTextArea _sendToIdTextField;

    private Timer connectionsUpdateTimer;
    private Timer receiver;

    private RestClient restClient;

    private EventBus eventBus;

    private SelectItemsFrame selectItemsFrame;

    public void init() throws IOException {
        selectItemsFrame = new SelectItemsFrame();

        restClient = new RestClient();

        connectionsListFrame = new ConnectionsListFrame();
        connectionsListFrame.init();

        receiver = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String response = restClient.doGet("/receive", id);
                    if (response != null && !response.isEmpty()) {
                        System.out.println(response);
                        _panel.deserializeFromJson(response);
                        selectItemsFrame.update(_panel.getObjects());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        connectionsUpdateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    restClient.doGet("/connections", id);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        _frame = new JFrame("Client: ");
        _frame.setTitle("Client ID: ожидание...");
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
        makeSelectItemsToSendButton();
        makeLaunchNewClientButton();

        _frame.add(_panel);

        _buttonPanel = new JPanel();
        _buttonPanel.add(_addRandomRectButton);
        _buttonPanel.add(_sendButton);
        _buttonPanel.add(_stopConnectionButton);
        _buttonPanel.add(_addRandomCircleButton);
        _buttonPanel.add(_showConnectionsButton);
        _buttonPanel.add(_sendToIdTextField);
        _buttonPanel.add(_selectItemsToSendButton);
        _buttonPanel.add(_launchNewClientButton);

        _frame.getContentPane().add(_buttonPanel, BorderLayout.SOUTH);

        eventBus = new EventBus();
        eventBus.register(this);

        try {
            id = restClient.register();
            _frame.setTitle("Client ID: " + id);
        } catch (Exception e1) {
            _frame.setTitle("Client connection failed");
            System.exit(1);
        }

        receiver.start();
        connectionsUpdateTimer.start();

        _frame.setVisible(true);
    }

    private void _makeAddRandomRectButton() {
        _addRandomRectButton = new JButton("Add Random Rect");
        _addRandomRectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _panel.addRandomRectFunc();
                selectItemsFrame.update(_panel.getObjects());
                _panel.repaint();
                try {
                    restClient.doPost("/put-new", id, _panel.serializeToJson());
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
    }

    private void _makeSendButton() {
        _sendButton = new JButton("Send");
        _sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    _sendTo(Integer.parseInt(_sendToIdTextField.getText()), _panel.serializeSelectedToJson(selectItemsFrame));
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
                selectItemsFrame.update(_panel.getObjects());
                _panel.repaint();
                try {
                    restClient.doPost("/put-new", id, _panel.serializeToJson());
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
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
        _sendToIdTextField = new JTextArea(1, 4);
        _sendToIdTextField.setSize(30, 10);
        _sendToIdTextField.setEditable(true);
    }

    private void _sendTo(int id, String json) {
        try {
            restClient.doPost("/send/" + id, this.id, json);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void exit() {
        connectionsUpdateTimer.stop();
        restClient.close();
        _frame.setVisible(false);
    }

    private void makeSelectItemsToSendButton() {
        _selectItemsToSendButton = new JButton("Select Items to Send");
        selectItemsFrame.init();
        _selectItemsToSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectItemsFrame.show(_panel.getObjects());
            }
        });
    }

    private void makeLaunchNewClientButton() {
        _launchNewClientButton = new JButton("Launch New Client");
        _launchNewClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MainFrame().init();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
