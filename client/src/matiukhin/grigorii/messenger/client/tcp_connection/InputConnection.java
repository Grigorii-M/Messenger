package matiukhin.grigorii.messenger.client.tcp_connection;

import matiukhin.grigorii.messenger.client.ResponseProcessor;
import matiukhin.grigorii.messenger.utilities.communication.responses.ConnectionErrorResponse;
import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class InputConnection extends SwingWorker<String, Response> {

    private final ObjectInputStream ois;
    private final ResponseProcessor responseProcessor;

    public InputConnection(ObjectInputStream ois, ResponseProcessor processor) {
        this.ois = ois;
        responseProcessor = processor;
    }

    @Override
    protected String doInBackground() throws Exception {
        try {
            System.out.println("Input - Connected");
            while (this.getState() != StateValue.DONE) {
                Response r = (Response) ois.readObject();
                publish(r);
                System.out.println("Received:" + r);
            }
        } catch (IOException e) {
            System.err.println("Input exception: " + e);
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