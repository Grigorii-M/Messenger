package matiukhin.grigorii.messenger.utilities.communication.responses;

import java.util.ArrayList;

public record GetConversationsResponse(ArrayList<String> conversationsForUser) implements Response {

    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
