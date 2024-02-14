package client;

import java.net.*;
import java.io.*;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

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

  private static long generateChecksum(String requestString) {
    byte [] m = requestString.getBytes();
    Checksum crc32 = new CRC32();
    crc32.update(m, 0, m.length);
    return crc32.getValue();
  }

  private static void sendRequest(DatagramSocket aSocket, String requestString, InetAddress aHost,
      int serverPort) throws IOException {

    // Parse request information from the request string.
    String[] requestToken = requestString.split("::");
    String action = requestToken[1];

    // creating datagram packet
    long requestId = generateChecksum(requestString);
    requestString = requestId + "::" + requestString;

    byte[] m = requestString.getBytes();
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
      long responseRequestId = Long.parseLong(responseToken[0]);

      // validating malformed responses from server
      if(responseRequestId != requestId) {
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
