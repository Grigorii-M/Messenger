package matiukhin.grigorii.messenger.server;

import matiukhin.grigorii.messenger.server.tcp_connection.Session;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class Server {

    private static final int PORT = 34522;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
	    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Session session = new Session(serverSocket.accept(), executorService);
                session.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
