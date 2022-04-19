package matiukhin.grigorii.messenger.client.ui;

import matiukhin.grigorii.messenger.utilities.communication.messages.LoginMessage;
import matiukhin.grigorii.messenger.utilities.communication.messages.Message;
import matiukhin.grigorii.messenger.client.MessageQueue;
import matiukhin.grigorii.messenger.utilities.communication.messages.SignupMessage;

import javax.swing.*;
import java.awt.*;

public class AuthenticationPanel extends JPanel {

    public AuthenticationPanel() {
        setLayout(new GridBagLayout());

        JLabel loginLabel = new JLabel("Username", SwingConstants.RIGHT);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 0, 2, 10);
        c.gridx = 0;
        c.gridy = 0;
        add(loginLabel, c);

        JLabel passwordLabel = new JLabel("Password", SwingConstants.RIGHT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        add(passwordLabel, c);

        JTextField loginTextField = new JTextField(10);
        c.insets = new Insets(2, 0, 2, 0);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 0;
        add(loginTextField, c);

        JTextField passwordTextField = new JPasswordField(10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 1;
        add(passwordTextField, c);

        JButton logInButton = new JButton("Log In");
        c.insets = new Insets(2, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 2;
        JPanel thisPanel = this;
        logInButton.addActionListener(actionEvent -> {
            if (loginTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(thisPanel,
                        "Username cannot be empty.",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE
                );
            } else if (passwordTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(thisPanel,
                        "Password cannot be empty.",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                Message message = new LoginMessage(loginTextField.getText(), passwordTextField.getText());
                MessageQueue.getInstance().add(message);
                loginTextField.setText("");
                passwordTextField.setText("");
            }
        });
        add(logInButton, c);

        JLabel orTextField = new JLabel("or");
        c.gridwidth = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 3;
        add(orTextField, c);

        JButton signUpButton = new JButton("Sign Up");
        c.insets = new Insets(2, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 4;
        signUpButton.addActionListener(actionEvent -> {
            JTextField usernameTextField = new JTextField();
            JTextField passwordTextField1 = new JPasswordField();
            JTextField passwordTextField2 = new JPasswordField();
            Object[] popupMessage = {
                    "Username:", usernameTextField,
                    "Password:", passwordTextField1,
                    "Repeat your password:", passwordTextField2
            };

            UIManager.put("OptionPane.yesButtonText", "Create account");
            UIManager.put("OptionPane.noButtonText", "Cancel");

            while (true) {

                int option = JOptionPane.showConfirmDialog(
                        thisPanel,
                        popupMessage,
                        "Sign up",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                UIManager.put("OptionPane.yesButtonText", "Yes");
                UIManager.put("OptionPane.noButtonText", "No");

                if (option == JOptionPane.YES_OPTION) {
                    if (usernameTextField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(thisPanel,
                                "Username cannot be empty.",
                                "Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else if (passwordTextField1.getText().isEmpty() || passwordTextField2.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(thisPanel,
                                "Password cannot be empty.",
                                "Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else if (!passwordTextField1.getText().equals(passwordTextField2.getText())) {
                        JOptionPane.showMessageDialog(thisPanel,
                                "Passwords do not match.",
                                "Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        Message message = new SignupMessage(usernameTextField.getText(), passwordTextField1.getText());
                        MessageQueue.getInstance().add(message);
                        break;
                    }
                } else {
                    break;
                }
            }


        });
        add(signUpButton, c);

        setMinimumSize(new Dimension(230, 200));
    }
}
