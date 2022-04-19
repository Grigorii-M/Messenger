package matiukhin.grigorii.messenger.utilities.communication.messages;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

public record LoadConversationWithMessage(String sender, String target) implements Message {
    @Override
    public Response accept(MessageVisitor visitor) {
        return visitor.visit(this);
    }
}
