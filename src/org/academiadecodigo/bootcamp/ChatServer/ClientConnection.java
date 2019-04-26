package org.academiadecodigo.bootcamp.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private Socket connectionSocket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String name = "";

    public ClientConnection(Socket connectionSocket, Server server, BufferedReader in, PrintWriter out) {
        this.connectionSocket = connectionSocket;
        this.server = server;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        this.init();
    }

    public void init() {
        System.out.println("New client request received.");
        this.showMenu();
        this.setName();
        this.start();
    }

    public void start() {
        String message = "";
        while (!message.equals("/exit")) {
            try {
                message = this.in.readLine();
                if (message.startsWith("/")) {
                    executeCommands(message);
                } else {
                    this.server.sendAll(this.name + ": " + message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        this.out.println(message);
    }

    public void executeCommands(String message) {
        switch (message) {
            case "/exit":
                this.exit();
            case "/users":
                this.server.showUsersConnected();
            default:
                System.out.println("");
        }
    }

    public String getName() {
        return this.name;
    }

    public String setName(){
        try {
            this.name = this.in.readLine();
            this.server.sendAll(this.name + " has joined the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.name;
    }

    public void exit() {
        try {
            System.out.println("Closing this connection : " + connectionSocket.getLocalAddress());
            this.connectionSocket.close();
            this.in.close();
            this.out.close();
            System.out.println("Connection closed");
        } catch (Exception e) {
            System.out.println("exception.");
        }
    }

    public void showMenu() {
        this.out.println("#### NUNO'S SERVER");
        this.out.println("#### ");
        this.out.println("#### /exit to exit ");
        this.out.println("#### /list to show clients connected ");
        this.out.println("#### ");
        this.out.println("#### Hello, what's your username?");
    }
}
