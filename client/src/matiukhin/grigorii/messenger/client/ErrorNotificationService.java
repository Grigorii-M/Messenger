package matiukhin.grigorii.messenger.client;

import matiukhin.grigorii.messenger.client.ui.MessengerFrame;

import javax.swing.*;

public class ErrorNotificationService {

    private static MessengerFrame reference;

    public static void assignReferenceFrame(MessengerFrame frame) {
        reference = frame;
    }

    public static void loginError(){
        JOptionPane.showMessageDialog(reference,
                "Could not username into the account.\nPlease try again.",
                "Login failure!",
                JOptionPane.INFORMATION_MESSAGE
        );
        reference.showAuthenticationUI();
    }

    public static void signupError() {
        JOptionPane.showMessageDialog(reference,
                "Could not create new account.\nPlease try again.",
                "Signup failure!",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void newConversationError() {
        JOptionPane.showMessageDialog(reference,
                "Could not create new conversation.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);
    }
}
