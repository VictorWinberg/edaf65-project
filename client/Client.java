package client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws Exception {
        String machine = args[0];
        int port = Integer.parseInt(args[1]);

        Socket socket = new Socket(machine, port);

        ClientSend send = new ClientSend(socket);
        send.start();

        ClientReceive receive = new ClientReceive(socket);
        receive.start();
    }
}

class ClientSend extends Thread {

    private Socket socket;

    public ClientSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
            OutputStream out = socket.getOutputStream();

            while (!socket.isClosed()) {
                String input = scanner.readLine();
                if (socket.isClosed()) {
                    System.out.println("Disconnected");
                } else {
                    out.write((input + '\n').getBytes());
                }
            }

            socket.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientReceive extends Thread {

    private Socket socket;

    public ClientReceive(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                String response = in.readLine();
                if (response == null) {
                    socket.close();
                    break;
                }
                System.out.println(response);
            }

            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}