package matiukhin.grigorii.messenger.client.ui;

import matiukhin.grigorii.messenger.client.ErrorNotificationService;
import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.client.User;
import matiukhin.grigorii.messenger.utilities.communication.messages.NewConversationMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConversationsPanel extends JPanel {

    private final JPanel panel;
    private final JButton addNewConversationButton;
    private final MainUI ui;
    private String conversationTarget;

    private final ArrayList<String> activeConversations;

    public ConversationsPanel(MainUI ui) {
        activeConversations = new ArrayList<>();
        this.ui = ui;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        add(scrollPane, c);

        addNewConversationButton = new JButton("New");
        addNewConversationButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        addNewConversationButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addNewConversationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel thisPanel = this;
        addNewConversationButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(thisPanel, "Enter targetUsername:", "Add new conversation", JOptionPane.INFORMATION_MESSAGE);
            if (input != null) {
                MessageQueue.getInstance().add(new NewConversationMessage(User.getInstance().getUsername(), input));
            }
        });
        panel.add(addNewConversationButton);
    }

    public void createNewConversation(String userName) {
        if (activeConversations.contains(userName)) {
            return;
        }
        activeConversations.add(userName);
        JButton temp = new JButton(userName);
        temp.addActionListener(e -> {
            conversationTarget = temp.getText();
            ui.notifyConversationSelected();
        });
        temp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        temp.setAlignmentX(Component.LEFT_ALIGNMENT);
        temp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(temp);
        panel.remove(addNewConversationButton);
        panel.add(addNewConversationButton);
        revalidate();
        repaint();
    }

    public void selectConversation(String userName) {
        conversationTarget = userName;
        ui.notifyConversationSelected();
    }

//    public void newConversationError() {
//        ErrorNotificationService.newConversationError();
//    }

    public String getCurrentConversationTarget() {
        return conversationTarget;
    }

    public boolean hasConversationWith(String user) {
        return activeConversations.contains(user);
    }

    public void reset() {
        activeConversations.clear();
        conversationTarget = null;
        panel.removeAll();
        revalidate();
        repaint();
    }
}
