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

        clientSocket.setSoTimeout(5000);

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
        } catch (IOException e) {
            System.err.println("Timeout occurred. Server did not respond within the specified time.");
        }
    }
}
 