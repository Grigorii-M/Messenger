package matiukhin.grigorii.messenger.utilities.communication.messages;

import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

public interface MessageVisitor {
    Response visit(TextMessage message);
    Response visit(LoginMessage message);
    Response visit(SignupMessage message);
    Response visit(NewConversationMessage message);
    Response visit(LoadActiveConversationsMessage message);
    Response visit(LoadConversationWithMessage message);
    Response visit(LogoutMessage message);
}
