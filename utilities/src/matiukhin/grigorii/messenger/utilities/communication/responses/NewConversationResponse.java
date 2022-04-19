package matiukhin.grigorii.messenger.utilities.communication.responses;

public record NewConversationResponse(boolean isSuccessful, String targetUsername) implements Response {
    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
