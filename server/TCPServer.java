package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends AbstractServer {

    @Override
    public void listen(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            System.out.println("Server is listening on port " + portNumber);
            serverLogger.log("Server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client.Client connected: " + clientSocket.getInetAddress());
                serverLogger.logRequest(clientSocket.getInetAddress(), "Client connected");

                try {
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                    System.out.println("client.Client disconnected");
                    serverLogger.log("Client disconnected: " + clientSocket.getInetAddress());

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
            StringBuilder requestBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                serverLogger.logRequest(clientSocket.getInetAddress(), inputLine);

                requestBuilder.append(inputLine).append("\n");

                String response = processRequest(inputLine);

                out.println(response);
                serverLogger.logResponse(clientSocket.getInetAddress(),response);
            }
        } catch (IOException e) {
            System.err.println("Timeout occurred. Server did not respond within the specified time.");
            serverLogger.logMalformedRequest(clientSocket.getInetAddress());

        }
    }
}
 