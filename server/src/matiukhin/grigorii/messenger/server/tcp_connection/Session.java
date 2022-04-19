package matiukhin.grigorii.messenger.server.tcp_connection;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class Session {
    private final Socket socket;
    private final ExecutorService executorService;
    private OutputConnection output;
    private InputConnection input;
    private final AtomicReference<ArrayDeque<Response>> sendQueue = new AtomicReference<>();

    public Session(Socket clientSocket, ExecutorService service) {
        socket = clientSocket;
        executorService = service;
        sendQueue.set(new ArrayDeque<>());
    }

    public void execute() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            output = new OutputConnection(oos, this);
            input = new InputConnection(ois, this);

            executorService.submit(output);
            executorService.submit(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void notifyException() {
        System.err.println("Shutdown");
        output.shutdown();
        input.shutdown();
    }

    public synchronized void addToOutputQueue(Response r) {
        if (r != null) {
            sendQueue.get().addLast(r);
        }
    }

    public synchronized boolean isOutputQueueEmpty() {
        return sendQueue.get().isEmpty();
    }

    public synchronized Response getResponse() {
        return sendQueue.get().removeFirst();
    }
}
