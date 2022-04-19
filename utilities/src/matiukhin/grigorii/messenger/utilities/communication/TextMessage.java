package matiukhin.grigorii.messenger.utilities.communication;

import matiukhin.grigorii.messenger.utilities.communication.messages.Message;
import matiukhin.grigorii.messenger.utilities.communication.messages.MessageVisitor;
import matiukhin.grigorii.messenger.utilities.communication.responses.Response;
import matiukhin.grigorii.messenger.utilities.communication.responses.ResponseVisitor;

public record TextMessage(String sender, String text, String targetUsername) implements Message, Response {

    @Override
    public Response accept(MessageVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(ResponseVisitor visitor) {visitor.visit(this);}
}
