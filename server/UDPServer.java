package server;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class UDPServer extends AbstractServer {
  @Override
  public void listen(int portNumber) {
    DatagramSocket aSocket = null;

    try {
      aSocket = new DatagramSocket(portNumber);
      byte[] buffer = new byte[1000];

      while(true) {
        DatagramPacket request = new DatagramPacket(buffer,
          buffer.length);
        aSocket.receive(request);

        String msg = new String(request.getData(), StandardCharsets.UTF_8);
        System.out.println(msg);
        String response = processRequest(msg);

        DatagramPacket reply = new DatagramPacket(response.getBytes(),
          response.getBytes().length, request.getAddress(), request.getPort());

        aSocket.send(reply);
      }
    }
    catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    }
    catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
    finally {
      if (aSocket != null)
        aSocket.close();
    }
  }

  @Override
  public void handleRequest(Socket clientSocket) throws IOException {

  }
}