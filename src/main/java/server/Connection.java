package server;

import frames.MainFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    private Socket _socket;
    private ServerSocket _serverSocket;
    private final boolean _isServer;

    private Thread _listener;

    private DataInputStream _in;
    private DataOutputStream _out;

    private static final int _PORT = 6666;

    public Connection(boolean isServer) {
        _socket = null;
        _serverSocket = null;
        _listener = null;
        _isServer = isServer;
        _in = null;
        _out = null;
    }

    public void connect(String ip, int port, MainFrame frame) throws IOException {
        if (_socket != null) {
            return;
        }
        System.out.println("Connecting to " + ip + ":" + port);
        _listener = new Thread() {
            public void run() {
                try {
                    System.out.println("Listening on " + ip + ":" + port);
                    if (!_isServer) {
                        _socket = new Socket(ip, port);
                    } else {
                        _serverSocket = new ServerSocket(port);
                        _socket = _serverSocket.accept();
                    }

                    _in = new DataInputStream(_socket.getInputStream());
                    _out = new DataOutputStream(_socket.getOutputStream());

                    while (true) {
                        String line = _in.readUTF();
                        if (line.equals("exit")) {
                            _listener.interrupt();
                            break;
                        } else if (line.equals("reserv")) {
                            frame.doClickOnCheckBox();
                            continue;
                        }
                        frame.paintObjectsFromSocket(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        _listener.start();
    }

    public void sendJsons(String[] json) {
        if (_socket == null) {
            return;
        }
        try {
            for (String s : json) {
                System.out.println(s);
                _out.writeUTF(s);
                _out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {
        if (_socket == null) {
            return;
        }
        try {
            _out.writeUTF(message);
            _out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
