package matiukhin.grigorii.messenger.utilities.communication.responses;

public record ConnectionErrorResponse(String cause) implements Response {
    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
