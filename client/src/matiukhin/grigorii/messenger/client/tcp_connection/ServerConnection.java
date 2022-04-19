package matiukhin.grigorii.messenger.client.tcp_connection;

import matiukhin.grigorii.messenger.client.ErrorNotificationService;
import matiukhin.grigorii.messenger.client.ResponseProcessor;
import matiukhin.grigorii.messenger.client.ui.MessengerFrame;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection {
    private final MessengerFrame frame;
    private final String address;
    private final int port;
    private final String filePath = "./network_config.txt";
    private OutputConnection output;
    private InputConnection input;

    public ServerConnection(String address, int port, MessengerFrame frame) {
        this.frame = frame;
        this.address = address;
        this.port = port;

        File configFile = new File(filePath);
        try (FileWriter usersFileWriter = new FileWriter(configFile)) {
            usersFileWriter.write(address + "\n");
            usersFileWriter.write(String.valueOf(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerConnection(MessengerFrame frame) throws FileNotFoundException{
        try (Scanner scanner = new Scanner(new File(filePath))) {
            address = scanner.nextLine();
            port = Integer.parseInt(scanner.nextLine());
            this.frame = frame;
        }
    }

    public void execute() {
        try {
            Socket socket = new Socket(address, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            ResponseProcessor responseProcessor = new ResponseProcessor(frame);
            output = new OutputConnection(oos, responseProcessor);
            input = new InputConnection(ois, responseProcessor);
            output.execute();
            input.execute();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            frame.establishNewConnection();
        }
    }

    public void cancel() {
        if (output != null) {
            output.cancel(false);
        }
        if (input != null) {
            input.cancel(false);
        }
    }
}
