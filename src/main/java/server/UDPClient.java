package server;

import com.google.common.eventbus.EventBus;
import events.ConnectionsReceivedEvent;
import events.IdReceivedEvent;
import events.ObjectReceivedEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private final String serverAddress;
    private final int serverPort;
    private DatagramSocket socket;
    private InetAddress address;
    private int clientId = -1;
    private final EventBus eventBus;

    public UDPClient(String serverAddress, int serverPort, EventBus eventBus) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.eventBus = eventBus;
    }

    public int init() throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(serverAddress);

        // Регистрация клиента на сервере
        send("REGISTER");

        // Запуск потока для приема сообщений
        new Thread(this::receiveLoop).start();

        return clientId;
    }

    private void receiveLoop() {
        byte[] buffer = new byte[65507];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());

                // Обработка присвоения ID
                if (message.startsWith("ID:")) {
                    this.clientId = Integer.parseInt(message.substring(3));
                    eventBus.post(new IdReceivedEvent(clientId));
                }

                // Обработка списка подключений
                if (message.startsWith("CONNECTIONS:")) {
                    eventBus.post(new ConnectionsReceivedEvent(message.substring(12)));
                    continue;
                }

                // Обработка входящих объектов
                eventBus.post(new ObjectReceivedEvent(message));

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void send(String message) throws IOException {
        byte[] data = message.getBytes();
        if (message.contains("send")) {
            System.out.println(message);
        }
        DatagramPacket packet = new DatagramPacket(data, data.length, address, serverPort);
        socket.send(packet);
    }

    public void close() {
        if (socket != null) {
            socket.close();
        }
    }
}