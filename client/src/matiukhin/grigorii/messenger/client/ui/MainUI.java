package matiukhin.grigorii.messenger.client.ui;

import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.client.User;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainUI extends JPanel {

    private final ConversationsPanel conversationsPanel;
    private final JLabel chatLabel;
    private final ChatPanels chatPanels;
    private String conversationTarget;

    public MainUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel conversationsLabel = new JLabel("Conversations");
        conversationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        conversationsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        c.gridy = 0;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 0.01;
        c.weighty = 0.01;
        add(conversationsLabel, c);

        conversationsPanel = new ConversationsPanel(this);
        c.gridy = 1;
        c.gridx = 0;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        add(conversationsPanel, c);

        chatLabel = new JLabel("Chat");
        chatLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chatLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        c.gridy = 0;
        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1;
        c.weighty = 0.01;
        add(chatLabel, c);

        chatPanels = new ChatPanels();
        JScrollPane messagesScrollPane = new JScrollPane(chatPanels);
        messagesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messagesScrollPane.getVerticalScrollBar().setUnitIncrement(12);
        c.gridy = 1;
        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        add(messagesScrollPane, c);

        JTextArea textField = new JTextArea();
        textField.setFont(new Font("Dialog", Font.PLAIN, 26));
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.01;
        c.gridwidth = GridBagConstraints.RELATIVE;
        add(textField, c);

        JButton sendButton = new JButton("Send");
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        sendButton.addActionListener(e -> {
            if (conversationTarget != null) {
                String text = textField.getText();
                if (!text.isEmpty()) {
                    TextMessage textMessage = new TextMessage(User.getInstance().getUsername(), text, conversationTarget);
                    textField.setText("");
                    chatPanels.addNewMessage(textMessage);
                    MessageQueue.getInstance().add(textMessage);
                }
            }
        });
        add(sendButton, c);
    }

    public void createNewConversation(String userName) {
        conversationsPanel.createNewConversation(userName);
        chatPanels.addNewConversation(userName);
        conversationsPanel.selectConversation(userName);
    }

    public void notifyConversationSelected() {
        conversationTarget = conversationsPanel.getCurrentConversationTarget();
        chatLabel.setText("Chat: " + conversationTarget);
        chatPanels.selectPanel(conversationTarget);
    }

    public void newIncomingText(TextMessage message) {
        if (!conversationsPanel.hasConversationWith(message.sender())) {
            conversationsPanel.createNewConversation(message.sender());
            chatPanels.addNewConversation(message.sender());
        }
        chatPanels.addNewMessage(message);
    }

    public void addNewConversationWith(String user) {
        conversationsPanel.createNewConversation(user);
    }

    public void addNewConversationWith(ArrayList<String> users) {
        for (String user : users) {
            conversationsPanel.createNewConversation(user);
            chatPanels.addNewConversation(user);
        }
    }

    public void updateConversation(ArrayList<TextMessage> messages, String target) {
        chatPanels.update(messages, target);
    }

    public void reset() {
        chatPanels.reset();
        conversationsPanel.reset();
        conversationTarget = null;
    }
}
