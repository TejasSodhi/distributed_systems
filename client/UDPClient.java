package client;

import java.net.*;
import java.io.*;
import java.util.UUID;

/**
 * This represents the UDP client which communicates to the UDP server over a given port and host
 * address.
 */
public class UDPClient {

  public static void main(String[] args) {

    // invalidate if inadequate number of arguments.
    if (args.length < 2) {
      System.out.println(
        "Usage: java UDPClient <Host name> <Port number>");
      System.exit(1);
    }

    try (DatagramSocket aSocket = new DatagramSocket()) {
      int n = 100;
      InetAddress aHost = InetAddress.getByName(args[0]);
      int serverPort = Integer.parseInt(args[1]);

      //Pre-populating key value store
      // Send PUT requests
      for (int i = 0; i < n; i++) {
        String putString = generateUUID() + "::PUT::key" + i + "::value" + i;
        sendRequest(aSocket, putString, aHost, serverPort);
      }

      // Send GET requests
      for (int i = 0; i < n; i++) {
        String getString = generateUUID() + "::GET::key" + i;
        sendRequest(aSocket, getString, aHost, serverPort);
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid arguments: " + e.getMessage());
    }
  }

  private static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private static void sendRequest(DatagramSocket aSocket, String requestString, InetAddress aHost,
      int serverPort) throws IOException {

    // Parse request information from the request string.
    String[] requestToken = requestString.split("::");
    String requestId = requestToken[0];
    String action = requestToken[1];

    // creating datagram packet
    byte [] m = requestString.getBytes();
    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

    // sending datagram packet
    aSocket.send(request);

    // setting timeout of 5 seconds for udp request and waiting for response from server
    aSocket.setSoTimeout(5000);
    byte[] buffer = new byte[1000];
    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

    try {
      // receive response
      aSocket.receive(reply);
      String response = new String(reply.getData(), 0, reply.getLength());
      String[] responseToken = response.split(":");

      // validating malformed responses from server
      if(!responseToken[0].equals(requestId)) {
        ClientLogger.log("Received Malformed response for request: " + requestId +
          " ; Received response for " + responseToken[0]);
      } else {
        ClientLogger.log("Received response " + response);
        System.out.println(action+" Reply: " + new String(reply.getData(), 0, reply.getLength()));
      }
    } catch(SocketTimeoutException e) {
      ClientLogger.log("Request timed out.. received no response from server for request: "
          + requestId);
    }
  }
}
