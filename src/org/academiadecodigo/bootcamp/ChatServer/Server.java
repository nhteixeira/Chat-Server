package org.academiadecodigo.bootcamp.ChatServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private CopyOnWriteArrayList<ClientConnection> clientConnections = new CopyOnWriteArrayList<>();
    private BufferedReader in;
    private PrintWriter out;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        Server newServer = new Server(8080);
        newServer.start();
    }

    public void start () {

        ServerSocket serverSocket = createServerSocket();

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                createStreams(socket);
                createClientConnection(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerSocket createServerSocket() {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createStreams(Socket socket) {
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void createClientConnection(Socket socket) {
        ClientConnection clientConnection = new ClientConnection(socket, this, this.in, this.out);
        this.clientConnections.add(clientConnection);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(clientConnection);
    }

    public void sendAll(String message) {
        for (ClientConnection clientConnection : clientConnections){
            clientConnection.sendMessage(message);
        }
    }

    public void showUsersConnected(){
        String names = "";
        for (ClientConnection clientConnection : clientConnections){
            names = names + clientConnection.getName() + "\n";
        }
        out.println(names);
    }
}
