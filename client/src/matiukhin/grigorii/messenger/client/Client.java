package matiukhin.grigorii.messenger.client;

import matiukhin.grigorii.messenger.client.ui.MessengerFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
public class Client {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(MessengerFrame::new);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
