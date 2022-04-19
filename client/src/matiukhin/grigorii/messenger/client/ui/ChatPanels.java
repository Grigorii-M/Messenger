package matiukhin.grigorii.messenger.client.ui;

import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.client.User;
import matiukhin.grigorii.messenger.utilities.Hasher;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.messages.LoadConversationWithMessage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatPanels extends JPanel {

    private static class ChatPanel extends JPanel {

        private final ChatPanels parent;
        private ChatPanel(ChatPanels parent) {
            this.parent = parent;
            setLayout(new GridBagLayout());
        }

        private int numberOfMessages = 0;

        private void addNewMessage(TextMessage textMessage) {
            JTextArea messageLabel = new JTextArea(textMessage.text());
            messageLabel.setEditable(false);
            if (messageLabel.getMinimumSize().width > 200) {
                messageLabel.setLineWrap(true);
                messageLabel.setWrapStyleWord(true);
                messageLabel.setColumns(15);
            }
            messageLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 26));

            GridBagConstraints cText = new GridBagConstraints();
            cText.weightx = 1;

            if (textMessage.sender().equals(User.getInstance().getUsername())) {
                // User's sent texts
                cText.anchor = GridBagConstraints.LINE_END;
                cText.insets = new Insets(10, 10, 10, 0);
            } else {
                // User's received texts
                cText.anchor = GridBagConstraints.LINE_START;
                cText.insets = new Insets(10, 0, 10, 10);
            }
            messageLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            cText.gridy = numberOfMessages;
            numberOfMessages++;

            add(messageLabel, cText);
            revalidate();
            repaint();
            parent.revalidatePanel();
        }
    }

    private final HashMap<String, ChatPanel> panels;

    public ChatPanels() {
        panels = new HashMap<>();
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("No conversation selected");
        label.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 18));
        add(label);
    }

    public void addNewMessage(TextMessage textMessage) {
        panels.get(Hasher.getConversationHash(textMessage.sender(), textMessage.targetUsername())).addNewMessage(textMessage);
    }

    public void update(ArrayList<TextMessage> messages, String target) {
        panels.get(Hasher.getConversationHash(User.getInstance().getUsername(), target)).removeAll();
        for (TextMessage m : messages) {
            addNewMessage(m);
        }
    }

    public void addNewConversation(String target) {
        panels.put(Hasher.getConversationHash(User.getInstance().getUsername(), target), new ChatPanel(this));
    }

    private void revalidatePanel() {
        revalidate();
        repaint();
    }

    public void selectPanel(String target) {
        removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(panels.get(Hasher.getConversationHash(User.getInstance().getUsername(), target)), c);
        revalidatePanel();
        MessageQueue.getInstance().add(new LoadConversationWithMessage(User.getInstance().getUsername(), target));
    }

    public void reset() {
        panels.clear();
        removeAll();
        revalidatePanel();
    }
}
