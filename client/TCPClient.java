package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;


public class TCPClient {
    private static final int TIMEOUT_DURATION = 1;
    private static final int NUM_OPERATIONS = 5;

    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java client.Client <serverIP> <port>");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

//        Socket socket = new Socket();
//        try{
//            socket.connect(new InetSocketAddress(serverIP, serverPort), 5000);
//        } catch (IOException e){
//            System.out.println("*************failed here*********");
//            e.printStackTrace();
//        }
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {

            System.out.println("Connected to the server");
            ClientLogger.log("Connected to the server");

            int n = 100;
            for(int i = 0; i < n; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();

                String putString = requestId + "::PUT::key" + i + "::value" + i;
                sendRequest(out, in, putString);
            }
//            //PUT 5 operations
//            for (int i = 1; i <= NUM_OPERATIONS; i++) {
//                sendRequest(out, in, "PUT key" + i + " value" + i);
//            }

            for(int i = 0; i < n; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();

                String getString = requestId + "::GET::key" + i;
                sendRequest(out, in, getString);
            }
//            //GET 5 operations
//            for (int i = 1; i <= NUM_OPERATIONS; i++) {
//                sendRequest(out, in, "GET key" + i);
//            }

            // DELETE 5 operations
//            for (int i = 1; i <= NUM_OPERATIONS; i++) {
//                sendRequest(out, in, "DELETE key" + i);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(PrintWriter out, BufferedReader in, String request) throws IOException {
        try{
            // Send request to server
            out.println(request);
            // Receive response from server
            String responseFromServer = in.readLine();
            System.out.println(responseFromServer);
            // Log response
            ClientLogger.log("Response from server: " + responseFromServer);

        } catch (SocketTimeoutException e){
            String[] strArr = request.split("::");
            String requestId = strArr[0];
            System.out.println("Received no response from the server for request id : "+requestId);
            ClientLogger.log("Received no response from the server for the request id : "+requestId);
        }
    }
}
