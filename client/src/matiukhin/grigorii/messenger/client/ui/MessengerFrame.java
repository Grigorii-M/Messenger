package matiukhin.grigorii.messenger.client.ui;

import matiukhin.grigorii.messenger.client.ErrorNotificationService;
import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.client.User;
import matiukhin.grigorii.messenger.client.tcp_connection.ServerConnection;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.messages.LogoutMessage;

import javax.swing.*;
import javax.swing.plaf.MenuBarUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MessengerFrame extends JFrame {

    private ServerConnection connection;
    private final JMenuBar menuBar;
    private final MainUI mainUI;
    private final AuthenticationPanel authPanel;
    private boolean isInConversationsMode;

    public MessengerFrame() {
        super("ABCMessenger");
        User.setApplicationFrame(this);

        ErrorNotificationService.assignReferenceFrame(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (connection != null) {
                    if (isInConversationsMode) {
                        MessageQueue.getInstance().add(new LogoutMessage(User.getInstance().getUsername()));
                    }
                    connection.cancel();
                }
            }
        });
        setSize(600, 600);
        setLocationRelativeTo(null);

        authPanel = new AuthenticationPanel();
        mainUI = new MainUI();

        menuBar = new JMenuBar();

        JMenu userMenu = new JMenu("User");
        userMenu.setMnemonic(KeyEvent.VK_U);

        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(e -> {
            User.logout();
            showAuthenticationUI();
            mainUI.reset();
        });
        userMenu.add(logoutMenuItem);

        menuBar.add(userMenu);

        showAuthenticationUI();

        setVisible(true);
        establishNewConnection();
    }

    public void showMessengerUI() {
        isInConversationsMode = true;
        setJMenuBar(menuBar);
        getContentPane().removeAll();
        add(mainUI);
        revalidate();
        repaint();
    }

    public void showAuthenticationUI() {
        isInConversationsMode = false;
        setJMenuBar(null);
        getContentPane().removeAll();
        add(authPanel);
        revalidate();
        repaint();
    }

    public void establishNewConnection() {
        JTextField serverTextField = new JTextField();
        JTextField portTextField = new JTextField();
        Object[] popupMessage = {
                "Server:", serverTextField,
                "Port:", portTextField
        };

        UIManager.put("OptionPane.yesButtonText", "Set new");
        UIManager.put("OptionPane.noButtonText", "Use previous");
        UIManager.put("OptionPane.cancelButtonText", "Exit");

        while (true) {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    popupMessage,
                    "Set new network settings",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                String serverAddress = serverTextField.getText();
                String portString = portTextField.getText();

                if (!portString.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this,
                            "Port number should contain only digits.",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (serverAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Address cannot be empty.",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    connection = new ServerConnection(serverAddress, Integer.parseInt(portString), this);
                    break;
                }
            } else if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                try {
                    connection = new ServerConnection(this);
                    break;
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(this,
                            "Could not find config file.",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                System.exit(0);
            }
        }

        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");

        connection.execute();
    }

    public void createNewConversation(String userName) {
        mainUI.createNewConversation(userName);
    }

    public void newIncomingText(TextMessage message) {
        mainUI.newIncomingText(message);
    }

    public void addNewConversationWith(String user) {
        mainUI.addNewConversationWith(user);
    }

    public void addNewConversationWith(ArrayList<String> users) {
        mainUI.addNewConversationWith(users);
    }

    public void updateConversation(ArrayList<TextMessage> messages, String target) {
        mainUI.updateConversation(messages, target);
    }
}
