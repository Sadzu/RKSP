package frames;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import events.ConnectionsReceivedEvent;
import events.IdReceivedEvent;
import events.ObjectReceivedEvent;
import server.UDPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame {
    private String ip;
    private int port;

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

    private UDPClient udpClient;

    private EventBus eventBus;

    private SelectItemsFrame selectItemsFrame;

    public void init(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;

        selectItemsFrame = new SelectItemsFrame();

        connectionsListFrame = new ConnectionsListFrame();
        connectionsListFrame.init();
        connectionsUpdateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    udpClient.send("gimmeConnections");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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

        udpClient = new UDPClient(ip, port, eventBus);
        udpClient.init();

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
        _sendToIdTextField = new JTextArea(1, 4);
        _sendToIdTextField.setSize(30, 10);
        _sendToIdTextField.setEditable(true);
    }

    private void _sendTo(int id, int count) {
        try {
            udpClient.send("send " + id + " " + count);
            for (String json : _panel.serializeSelectedToJson(selectItemsFrame)) {
                udpClient.send(json);
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
        connectionsUpdateTimer.stop();
        udpClient.close();
        _frame.setVisible(false);
    }

    private void makeSelectItemsToSendButton() {
        _selectItemsToSendButton = new JButton("Select Items to Send");
        selectItemsFrame.init(_panel.getObjects());
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
                    new MainFrame().init(ip, port + 1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    public void onIdReceived(IdReceivedEvent event) {
        _frame.setTitle("Client ID: " + event.getId());
    }
}
