package matiukhin.grigorii.messenger.client;

import matiukhin.grigorii.messenger.client.ui.MessengerFrame;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.responses.*;

public class ResponseProcessor implements ResponseVisitor {

    private final MessengerFrame frame;

    public ResponseProcessor(MessengerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void visit(LoginResponse response) {
        if (response.isSuccessful()) {
            User.login(response.userName());
            frame.showMessengerUI();
        } else {
            //frame.notifyUnsuccessfulLogin();
            ErrorNotificationService.loginError();
        }
    }

    @Override
    public void visit(SignupResponse response) {
        if (!response.isSuccessful()) {
            //frame.notifyUnsuccessfulSignup();
            ErrorNotificationService.signupError();
        }
    }

    @Override
    public void visit(ConnectionErrorResponse response) {
        frame.showAuthenticationUI();
        frame.establishNewConnection();
    }

    @Override
    public void visit(NewConversationResponse response) {
        if (response.isSuccessful()) {
            frame.createNewConversation(response.targetUsername());
        } else {
            //frame.notifyNewConversationError();
            ErrorNotificationService.newConversationError();
        }
    }

    @Override
    public void visit(TextMessage message) {
        frame.newIncomingText(message);
    }

    @Override
    public void visit(GetConversationsResponse response) {
        frame.addNewConversationWith(response.conversationsForUser());
    }

    @Override
    public void visit(GetConversationWithAnotherUserResponse response) {
        frame.updateConversation(response.messages(), response.target());
    }
}
