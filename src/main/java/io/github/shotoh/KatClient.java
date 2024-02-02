package io.github.shotoh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class KatClient extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public KatClient(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            KatServer.CLIENTS.add(this);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                List<KatClient> clients = KatServer.CLIENTS;
                synchronized (clients) {
                    System.out.println(inputLine);
                    for (KatClient client : clients) {
                        if (this.equals(client)) continue;
                        client.send(inputLine);
                    }
                }
            }
            close();
        } catch (SocketException e) {
            try {
                close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String s) {
        if (this.isAlive() && out != null) {
            out.println(s);
        }
    }

    public void close() throws IOException {
        KatServer.CLIENTS.remove(this);
        in.close();
        out.close();
        clientSocket.close();
    }
}