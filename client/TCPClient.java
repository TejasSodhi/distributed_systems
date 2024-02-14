package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;


public class TCPClient {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java client.Client <serverIP> <port>");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverIP, serverPort)) {
            socket.setSoTimeout(5000);

            System.out.println("Connected to the server");
            ClientLogger.log("Connected to the server");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // prepopulate the data
            populateKeyValues(in, out);

            // this code snippet is used to make the client interactive
            while (true) {
                System.out.println("Which operation do you want to use?");
                System.out.println("1. PUT");
                System.out.println("2. GET");
                System.out.println("3. DELETE");
                System.out.print("Enter your choice (1/2/3): ");
                String choice = userInput.readLine();

                String operation;
                switch (choice) {
                    case "1":
                        operation = "PUT";
                        break;
                    case "2":
                        operation = "GET";
                        break;
                    case "3":
                        operation = "DELETE";
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        continue;
                }

                if (operation.equals("PUT")) {
                    System.out.print("Please enter the key (integer): ");
                    String key = userInput.readLine();
                    System.out.print("Please enter the value for the key (integer): ");
                    String value = userInput.readLine();
                    String requestId = UUID.randomUUID().toString();
                    String request = requestId + "::" + operation + "::" + key + "::value" + value;
                } else {
                    System.out.print("Please enter the key (integer): ");
                    String key = userInput.readLine();
                    String requestId = UUID.randomUUID().toString();
                    String request = requestId + "::" + operation + "::" + key;
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

    private static void populateKeyValues(BufferedReader in, PrintWriter out) {

        NUM_KEYS = 10
        try {
            // PUT requests
            for (int i = 1; i <= NUM_KEYS; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String value = Integer.toString(i * 10);
                String putString = requestId + "::PUT::key" + key + "::value" + value;

                sendRequest(out, in, putString);
                System.out.println("Prepopulated key " + key + " with value " + value);
                ClientLogger.log("Prepopulated key " + key + " with value " + value);
            }
            //GET requests
            for (int i = 1; i <= NUM_KEYS; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String getString = requestId + "::GET::key" + key;

                sendRequest(out, in, getString);
                System.out.println("Prepopulated key " + key + " with value " + value);
                ClientLogger.log("Prepopulated key " + key + " with value " + value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
