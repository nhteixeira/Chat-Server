package org.academiadecodigo.bootcamp.ChatServer;

import java.io.*;
import java.net.Socket;

public class Client {

    private int serverPort;

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        Client nuno = new Client(8080);
        nuno.start();
    }

    public void start() {

        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.println(in.readLine());
                String toSend = terminalIn.readLine();
                out.write(toSend);

                String received = in.readLine();
                System.out.println(received);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
