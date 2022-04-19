package matiukhin.grigorii.messenger.client;

import matiukhin.grigorii.messenger.utilities.communication.messages.Message;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicReference;

public class MessageQueue {

    private static volatile MessageQueue instance;

    public synchronized static MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }

        return instance;
    }

    private final AtomicReference<ArrayDeque<Message>> queue = new AtomicReference<>();
    private final AtomicReference<ArrayDeque<Message>> memory = new AtomicReference<>();

    private MessageQueue() {
        queue.set(new ArrayDeque<>());
        memory.set(new ArrayDeque<>());
    }

    public boolean isEmpty() {
        return queue.get().isEmpty();
    }

    public synchronized Message remove() {
        Message m = queue.get().removeFirst();
        memory.get().addLast(m);
        return m;
    }

    public synchronized Message removeFromMemory() {
        return memory.get().removeFirst();
    }

    public synchronized void add(Message m) {
        queue.get().addLast(m);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Message m : queue.get()) {
            str.append(m.toString()).append(",");
        }
        if (!str.isEmpty()) {
            str.delete(str.length() - 1, str.length());
        }
        str.insert(0, "{");
        str.append("}");
        return str.toString();
    }
}
