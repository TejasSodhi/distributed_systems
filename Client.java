import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <serverIP> <port> <protocol>");
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

            // Example: send PUT request
            out.println("PUT key value");

            String responseFromServer = in.readLine();
            System.out.println(responseFromServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
