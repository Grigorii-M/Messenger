package matiukhin.grigorii.messenger.client.tcp_connection;

import matiukhin.grigorii.messenger.client.ResponseProcessor;
import matiukhin.grigorii.messenger.utilities.communication.messages.Message;
import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.utilities.communication.responses.ConnectionErrorResponse;
import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class OutputConnection extends SwingWorker<String, Response> {

    private final ObjectOutputStream oos;
    private final ResponseProcessor responseProcessor;

    public OutputConnection(ObjectOutputStream oos, ResponseProcessor responseProcessor) {
        this.oos = oos;
        this.responseProcessor = responseProcessor;
    }

    @Override
    protected String doInBackground() throws Exception {
        try {
            System.out.println("Output - Connected");
            MessageQueue queue = MessageQueue.getInstance();
            while (this.getState() != StateValue.DONE) {
                if (!queue.isEmpty()) {
                    Message m = queue.remove();

                    oos.writeObject(m);
                    System.out.println("Sent:" + m);
                }
            }
        } catch (IOException e) {
            System.err.println("Output exception: " + e);
            publish(new ConnectionErrorResponse("CONNECTION_ERROR"));
        }
        return null;
    }

    @Override
    protected void process(List<Response> responses) {
        for (Response response : responses) {
            response.accept(responseProcessor);
        }
    }
}