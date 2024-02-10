package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements IServer {
    private static final KeyValueStore keyValueStore = new KeyValueStore();

    @Override
    public void listen(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client.Client connected: " + clientSocket.getInetAddress());

                try {
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                    System.out.println("client.Client disconnected");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(Socket clientSocket) throws IOException {
        try (
          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                String response = processRequest(inputLine);

                out.println(response);
            }
        }
    }

    public String processRequest(String inputLine) {
        String[] tokens = inputLine.split(" ");
        if (tokens.length < 2) {
            return "Invalid request format";
        }

        String operation = tokens[0];
        String key = tokens[1];
        String value = tokens.length > 2 ? tokens[2] : null;

        switch (operation.toUpperCase()) {
            case "PUT":
                if (value == null) {
                    return "PUT operation requires a value";
                }
                keyValueStore.put(key, value);
                return "Key '" + key + "' stored with value '" + value + "'";
            case "GET":
                String storedValue = keyValueStore.get(key);
                return (storedValue != null) ? "Value for key '" + key + "': " + storedValue : "Key '" + key + "' not found";
            case "DELETE":
                String removedValue = keyValueStore.delete(key);
                return (removedValue != null) ? "Deleted key '" + key + "' with value '" + removedValue + "'" : "Key '" + key + "' not found";
            default:
                return "Unsupported operation: " + operation;
        }
    }
}
