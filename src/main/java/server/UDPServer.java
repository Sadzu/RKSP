package server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import events.ObjectReceivedEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UDPServer {
    private final int port;
    private DatagramSocket socket;
    private final Map<Integer, ClientInfo> clients = new HashMap<>();
    private final AtomicInteger lastClientId = new AtomicInteger(0);
    private final EventBus eventBus = new EventBus();

    public UDPServer(int port) {
        this.port = port;
    }

    public void start() throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println("Server started on port " + port);

        new Thread(this::receiveLoop).start();
    }

    private void receiveLoop() {
        byte[] buffer = new byte[65507]; // Максимальный размер UDP пакета

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();

                // Обработка регистрации нового клиента
                if (message.startsWith("REGISTER")) {
                    int clientId = lastClientId.incrementAndGet();
                    clients.put(clientId, new ClientInfo(address, clientPort));
                    sendToClient(clientId, "ID:" + clientId);
                    broadcastConnectionsList();
                    continue;
                }

                // Обработка запроса списка подключений
                if (message.equals("gimmeConnections")) {
                    int clientId = findClientId(address, clientPort);
                    if (clientId != -1) {
                        broadcastConnectionsList();
                    }
                    continue;
                }

                // Обработка команды отправки объекта конкретному клиенту
                if (message.startsWith("send ")) {
                    System.out.println(message);
                    String[] parts = message.split(" ");
                    int targetId = Integer.parseInt(parts[1]);
                    int count = Integer.parseInt(parts[2]);

                    for (int i = 0; i < count; i++) {
                        socket.receive(packet);
                        String objectMessage = new String(packet.getData(), 0, packet.getLength());
                        sendToClient(targetId, objectMessage);
                    }

                    continue;
                }

                // Обработка обычного сообщения (пересылка всем)
                int senderId = findClientId(address, clientPort);
                if (senderId != -1) {
                    eventBus.post(new ObjectReceivedEvent(message));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int findClientId(InetAddress address, int port) {
        return clients.entrySet().stream()
                .filter(entry -> entry.getValue().matches(address, port))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    private void sendToClient(int clientId, String message) {
        ClientInfo client = clients.get(clientId);
        if (client != null) {
            try {
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, client.address, client.port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastConnectionsList() {
        StringBuilder sb = new StringBuilder("CONNECTIONS:");
        clients.forEach((id, info) -> sb.append(id).append(","));
        String message = sb.toString();

        clients.forEach((id, info) -> sendToClient(id, message));
    }

    @Subscribe
    public void onObjectReceived(ObjectReceivedEvent event) {
        // Логика обработки полученных объектов
    }

    private static class ClientInfo {
        final InetAddress address;
        final int port;

        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        boolean matches(InetAddress address, int port) {
            return this.address.equals(address) && this.port == port;
        }
    }

    public static void main(String[] args) throws SocketException {
        UDPServer server = new UDPServer(5000);
        server.start();
    }
}