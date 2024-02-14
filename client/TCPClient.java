package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class TCPClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java client.Client <serverIP> <port>");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

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

            for(int i = 0; i < n; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();

                String getString = requestId + "::GET::key" + i;
                sendRequest(out, in, getString);
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
}
