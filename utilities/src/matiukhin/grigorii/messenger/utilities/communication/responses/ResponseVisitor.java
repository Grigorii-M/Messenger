package matiukhin.grigorii.messenger.utilities.communication.responses;

import matiukhin.grigorii.messenger.utilities.communication.TextMessage;

public interface ResponseVisitor {
    void visit(LoginResponse response);
    void visit(SignupResponse response);
    void visit(ConnectionErrorResponse response);
    void visit(NewConversationResponse response);
    void visit(TextMessage message);
    void visit(GetConversationsResponse response);
    void visit(GetConversationWithAnotherUserResponse response);
}
