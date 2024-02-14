package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class TCPClient extends AbstractClient {
    public void startClient(String serverIP, int serverPort) {
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
            populateKeyValues(in, out);

            while (true) {
                String request = generateRequestFromUserChoice(userInput);
                if(request.isEmpty()) {
                    continue;
                }
                sendRequest(out, in, request);

                System.out.print("Do you want to perform another operation? (yes/no): ");
                String anotherOperation = userInput.readLine().toLowerCase();
                if (!anotherOperation.equals("yes")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long generateChecksum(String requestString) {
        byte [] m = requestString.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(m, 0, m.length);
        return crc32.getValue();
    }

    private static void sendRequest(PrintWriter out, BufferedReader in, String request) throws IOException {
        try{
            request = generateChecksum(request) + "::" + request;
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

    private static void populateKeyValues(BufferedReader in, PrintWriter out) {
        final int NUM_KEYS = 10;
        try {
            // PUT requests
            for (int i = 1; i <= NUM_KEYS; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String value = Integer.toString(i * 10);
                String putString = requestId + "::PUT::key" + key + "::value" + value;

                sendRequest(out, in, putString);
                System.out.println("Pre-populated key" + key + " with value " + value);
                ClientLogger.log("Pre-populated key" + key + " with value " + value);
            }
            //GET requests
            for (int i = 1; i <= NUM_KEYS; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String getString = requestId + "::GET::key" + key;

                sendRequest(out, in, getString);
                System.out.println("GET key" + key);
                ClientLogger.log("GET key" + key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
