package matiukhin.grigorii.messenger.utilities.communication.responses;

public record LoginResponse(boolean isSuccessful, String userName) implements Response {
    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
