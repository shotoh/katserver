package io.github.shotoh;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KatServer {
    public static final List<KatClient> CLIENTS = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        int count = 0;
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(System.getenv("KAT_PORT")))) {
            System.out.println("Server socket established");
            System.out.println("Waiting for connections...");
            while (true) {
                new KatClient(serverSocket.accept()).start();
                count++;
                System.out.println(count + " clients have connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}