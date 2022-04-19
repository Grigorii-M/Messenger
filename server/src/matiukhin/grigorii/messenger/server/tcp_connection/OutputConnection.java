package matiukhin.grigorii.messenger.server.tcp_connection;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutputConnection implements Runnable {

    private final ObjectOutputStream oos;
    private final Session session;
    private boolean shouldShutdown = false;

    public OutputConnection(ObjectOutputStream oos, Session session) {
        this.oos = oos;
        this.session = session;
    }

    @Override
    public void run() {
        System.out.println("Output - Connected");
        try {
            while (!shouldShutdown) {
                if (!session.isOutputQueueEmpty()) {
                    Response r = session.getResponse();
                    System.out.println("Sent: " + r);
                    oos.writeObject(r);
                } else {
                    // For some unknown reason the code inside if block does not execute
                    // without else statement or some code before or after it
                    System.out.print("");
                }
            }
        } catch (IOException e) {
            System.err.println("Output exception: " + e);
            session.notifyException();
        }
    }

    public synchronized void shutdown() {
        shouldShutdown = true;
    }
}
