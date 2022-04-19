package matiukhin.grigorii.messenger.utilities.communication.messages;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

public record LoginMessage(String login, String password) implements Message {
    @Override
    public Response accept(MessageVisitor visitor) {
        return visitor.visit(this);
    }
}
