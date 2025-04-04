package server;

import com.google.common.eventbus.EventBus;
import events.ConnectionsReceivedEvent;
import events.ObjectReceivedEvent;
import frames.MainFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private class ClientNet {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        private Thread listener;
        private boolean isOpened;

        private int id;

        public ClientNet(Socket socket) throws IOException {
            try {
                this.socket = socket;
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            listener = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (!isOpened) {
                                break;
                            }
                            String message = in.readUTF();
                            if (message.equals("exit")) {
                                close();
                                break;
                            } else if (message.equals("connections")) {
                                String connections = in.readUTF();
                                connectionsToFrame(connections);
                            } else {
                                deserialize(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            id = Integer.parseInt(in.readUTF().split(" ")[1]);
            isOpened = true;
            listener.start();
        }
    }
    private ClientNet clientNet;

    private EventBus eventBus;

    public int init(EventBus eventBus) throws IOException {
        clientNet = new ClientNet(new Socket(Server.IP, Server.PORT));
        this.eventBus = eventBus;
        return clientNet.id;
    }

    public void deserialize(String json) {
        eventBus.post(new ObjectReceivedEvent(json));
    }

    public void send(String message) throws IOException {
        clientNet.out.writeUTF(message);
        clientNet.out.flush();
    }

    public void connectionsToFrame(String connections) {
        eventBus.post(new ConnectionsReceivedEvent(connections));
    }

    public void close() throws IOException {
        clientNet.isOpened = false;
        clientNet.listener.interrupt();
        clientNet.out.writeUTF("exit");
        clientNet.out.flush();
        clientNet.socket.close();
    }
}
