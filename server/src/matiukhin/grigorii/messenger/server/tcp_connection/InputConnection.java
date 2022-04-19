package matiukhin.grigorii.messenger.server.tcp_connection;

import matiukhin.grigorii.messenger.server.Database;
import matiukhin.grigorii.messenger.server.MessageProcessor;
import matiukhin.grigorii.messenger.utilities.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputConnection implements Runnable {

    private final ObjectInputStream ois;
    private final Session session;
    private boolean shouldShutdown = false;

    private final MessageProcessor processor;

    public InputConnection(ObjectInputStream ois, Session session) {
        this.ois = ois;
        this.session = session;
        processor = new MessageProcessor(Database.getInstance(), session);
    }

    @Override
    public void run() {
        System.out.println("Input - Connected");
        try {
            while (!shouldShutdown) {
                Message m = (Message) ois.readObject();
                System.out.println("Received: " + m);
                session.addToOutputQueue(m.accept(processor));
                System.out.println("Done processing");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Input exception: " + e);
            session.notifyException();
        }
    }

    public synchronized void shutdown() {
        shouldShutdown = true;
    }
}
