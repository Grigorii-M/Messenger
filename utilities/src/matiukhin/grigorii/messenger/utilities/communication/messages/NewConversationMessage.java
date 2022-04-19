package matiukhin.grigorii.messenger.utilities.communication.messages;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

public record NewConversationMessage(String sender, String targetUsername) implements Message {

    public String getTargetUsername() {
        return targetUsername;
    }

    @Override
    public Response accept(MessageVisitor visitor) {
        return visitor.visit(this);
    }
}
