package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static class ServerNet {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        private Thread listener;

        private int id;

        public ServerNet(Socket socket, int id) throws IOException {
            this.socket = socket;
            this.id = id;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String message = in.readUTF();
                            if (message.equals("exit")) {
                                out.writeUTF("exit");
                                out.flush();
                                close();
                                break;
                            }
                            if (message.contains("send")) { //send id count
                                int id = Integer.parseInt(message.split(" ")[1]);
                                int count = Integer.parseInt(message.split(" ")[2]);
                                for (int i = 0; i < count; i++) {
                                    sendTo(id, in.readUTF());
                                }
                                continue;
                            }
                            if (message.contains("gimmeConnections")) {
                                out.writeUTF("connections");
                                out.flush();
                                out.writeUTF(clients.keySet().toString());
                                out.flush();
                            }
                        } catch (Exception e) {
                            try {
                                close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        }
                    }
                }
            });
        }

        private void close() throws IOException {
            listener.interrupt();
            socket.close();
            in.close();
            out.close();
            Server.clients.remove(id);
        }
    }

    private static HashMap<Integer, ServerNet> clients = new HashMap<Integer, ServerNet>();
    private static ServerSocket serverSocket;
    private static int globalMaxId;
    public static int PORT = 6666;
    public static String IP = "127.0.0.1";
    private static Thread accepter;

    public static void main(String[] args) throws IOException {
        clients = new HashMap<>();
        globalMaxId = 0;
        serverSocket = new ServerSocket(PORT);
        accepter = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        clients.put(++globalMaxId, new ServerNet(socket, globalMaxId));
                        clients.get(globalMaxId).listener.start();
                        clients.get(globalMaxId).out.writeUTF("yourid " + globalMaxId);
                        clients.get(globalMaxId).out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        accepter.start();
    }

    public static void sendTo(int id, String json) {
        try {
            clients.get(id).out.writeUTF(json);
            clients.get(id).out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
