package matiukhin.grigorii.messenger.server;

import matiukhin.grigorii.messenger.server.tcp_connection.Session;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.messages.*;
import matiukhin.grigorii.messenger.utilities.communication.responses.*;

public class MessageProcessor implements MessageVisitor {

    private final Database database;
    private final Session session;

    public MessageProcessor(Database database, Session session) {
        this.database = database;
        this.session = session;
    }

    @Override
    public Response visit(TextMessage message) {
        database.addNewMessage(message.sender(), message.targetUsername(), message.text());
        //return new TextMessage(message.targetUsername(), message.text(), message.sender());
        return null;
    }

    @Override
    public Response visit(LoginMessage message) {
        return new LoginResponse(database.validateLogin(message.login(), message.password(), session), message.login());
    }

    @Override
    public Response visit(SignupMessage message) {
        return new SignupResponse(database.addNewUser(message.username(), message.password()));
    }

    @Override
    public Response visit(NewConversationMessage message) {
        return new NewConversationResponse(database.createNewConversation(message.sender(), message.getTargetUsername()), message.getTargetUsername());
    }

    @Override
    public Response visit(LoadActiveConversationsMessage message) {
        return new GetConversationsResponse(database.getConversationsForUser(message.sender()));
    }

    @Override
    public Response visit(LoadConversationWithMessage message) {
        return new GetConversationWithAnotherUserResponse(database.getConversationBetween(message.sender(), message.target()), message.target());
    }

    @Override
    public Response visit(LogoutMessage message) {
        database.userLogout(message.sender());
        return null;
    }
}
