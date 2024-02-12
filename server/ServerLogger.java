package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLogger {
    private static final String LOG_FILE = "server_log.txt";

    public static void logRequest(SocketAddress clientAddress, String request) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Request from " + clientAddress + ": " + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logResponse(String response) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logMalformedRequest(SocketAddress clientAddress, int packetLength) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Malformed request from " + clientAddress + " of length " + packetLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
