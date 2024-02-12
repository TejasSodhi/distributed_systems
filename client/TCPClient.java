package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
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

            for(int i = 0; i < n; i++) {
                String putString = "GET key" + i;
                out.println(putString);
                String responseFromServer = in.readLine();
                System.out.println(responseFromServer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
