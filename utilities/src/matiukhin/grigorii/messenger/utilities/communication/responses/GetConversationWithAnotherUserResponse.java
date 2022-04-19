package matiukhin.grigorii.messenger.utilities.communication.responses;

import matiukhin.grigorii.messenger.utilities.communication.TextMessage;

import java.util.ArrayList;

public record GetConversationWithAnotherUserResponse(ArrayList<TextMessage> messages, String target) implements Response {
    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
