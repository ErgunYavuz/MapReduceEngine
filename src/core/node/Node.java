package core.node;
import java.io.*;
import java.net.*;

public abstract class Node {
    protected ServerSocket serverSocket;
    protected String address;
    protected int port;

    public Node(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    protected void send(String address, int port, Object data) throws IOException {
        try (Socket socket = new Socket(address, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(data);
        }
    }

    protected Object receive() throws IOException, ClassNotFoundException {
        try (Socket clientSocket = serverSocket.accept();
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            return in.readObject();
        }
    }
}