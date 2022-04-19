package matiukhin.grigorii.messenger.client;

import matiukhin.grigorii.messenger.client.ui.MessengerFrame;
import matiukhin.grigorii.messenger.utilities.communication.messages.LoadActiveConversationsMessage;
import matiukhin.grigorii.messenger.utilities.communication.messages.LogoutMessage;

public class User {
    private static User instance;
    private static MessengerFrame frame;

    public static User getInstance() {
        if (instance == null) {
            // Todo: kick out to the auth panel
            System.err.print("User undefined");
        }

        return instance;
    }

    public static void login(String userName) {
        instance = new User(userName);
        frame.setTitle(frame.getTitle() + ": " + userName);
    }

    private final String userName;

    private User(String userName) {
        this.userName = userName;
        MessageQueue.getInstance().add(new LoadActiveConversationsMessage(this.userName));
    }

    public static void logout() {
        MessageQueue.getInstance().add(new LogoutMessage(instance.userName));
        frame.setTitle(frame.getTitle().replaceFirst(": " + getInstance().userName, ""));
        instance = null;
    }

    public String getUsername() {
        return userName;
    }

    public static void setApplicationFrame(MessengerFrame f) {
        frame = f;
    }
}
