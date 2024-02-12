package client;

import java.net.*;
import java.io.*;

public class UDPClient {

  public static void main(String[] args) {
    DatagramSocket aSocket = null;
    if (args.length < 2) {
      System.out.println(
        "Usage: java UDPClient <Host name> <Port number>");
      System.exit(1);
    }

    try {
      aSocket = new DatagramSocket();
      int n = 100;
      InetAddress aHost = InetAddress.getByName(args[0]);
      int serverPort = Integer.parseInt(args[1]);

      // Send PUT requests
      for(int i = 0; i < n; i++) {
        String putString = "PUT key" + i + " value" + i;
        byte [] m = putString.getBytes();
        DatagramPacket request =
          new DatagramPacket(m, m.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        System.out.println("PUT Reply: " + new String(reply.getData(), 0, reply.getLength()));
      }

      // Send GET requests
      for(int i = 0; i < n; i++) {
        String getString = "GET key" + i;
        byte [] m = getString.getBytes();
        DatagramPacket request =
          new DatagramPacket(m, m.length, aHost, serverPort);
        aSocket.send(request);
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);
        System.out.println("GET Reply: " + new String(reply.getData()));
      }
    }
    catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    }
    catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + e.getMessage());
    }
    catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid arguments: " + e.getMessage());
    }
    finally {
      if (aSocket != null)
        aSocket.close();
    }
  }
}
