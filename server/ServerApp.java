package server;

public class ServerApp {
  public static void main(String[] args) {
    //TODO: Add UDP port as well.
    if (args.length != 1) {
      System.out.println("Usage: java Server <tcp-port>");
      System.exit(1);
    }

    int tcpPortNumber = Integer.parseInt(args[0]);

    // Instantiate TCP and UDP servers.
    IServer tcpServer = new TCPServer();
    tcpServer.listen(tcpPortNumber);
  }
}
