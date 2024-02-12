package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class TCPClient {
    private static final int TIMEOUT_DURATION = 5000;
    private static final int NUM_OPERATIONS = 5;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java client.Client <serverIP> <port> <protocol>");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String protocol = args[2].toUpperCase();

        try (
            Socket socket = new Socket(serverIP, serverPort);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("Connected to the server");

            int n = 100;
            for(int i = 0; i < n; i++) {
                String putString = "PUT key" + i + " value" + i;
                out.println(putString);
                String responseFromServer = in.readLine();
                System.out.println(responseFromServer);
            }
            //PUT 5 operations
            for (int i = 1; i <= NUM_OPERATIONS; i++) {
                sendRequest(out, in, "PUT key" + i + " value" + i);
            }

            for(int i = 0; i < n; i++) {
                String putString = "GET key" + i;
                out.println(putString);
                String responseFromServer = in.readLine();
                System.out.println(responseFromServer);
            }
            //GET 5 operations
            for (int i = 1; i <= NUM_OPERATIONS; i++) {
                sendRequest(out, in, "GET key" + i);
            }

            // DELETE 5 operations
            for (int i = 1; i <= NUM_OPERATIONS; i++) {
                sendRequest(out, in, "DELETE key" + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(PrintWriter out, BufferedReader in, String request) throws IOException {
        Thread timerThread = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_DURATION);
                System.err.println("Timeout occurred. No response from server for request: " + request);
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timerThread.start();

        // Send request to server
        out.println(request);

        // Receive response from server
        String responseFromServer = in.readLine();
        System.out.println(responseFromServer);

        // Interrupt the timer thread as response received within timeout duration
        timerThread.interrupt();
    }
}
