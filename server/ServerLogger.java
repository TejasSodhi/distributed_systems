package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLogger {
    private static final String LOG_FILE = "server_log.txt";

    public void log(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logRequest(InetAddress clientAddress, String request) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Request from : " + clientAddress + ": " + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logResponse(InetAddress clientAddress, String response) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Response to : " + clientAddress + ": " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logMalformedRequest(InetAddress clientAddress, int packetLength) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Received Malformed request from " + clientAddress + " of length " + packetLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logMalformedRequest(InetAddress clientAddress) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            writer.println(timeStamp + " - Received Malformed request from " + clientAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
