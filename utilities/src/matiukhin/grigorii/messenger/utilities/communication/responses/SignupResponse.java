package matiukhin.grigorii.messenger.utilities.communication.responses;

public record SignupResponse(boolean isSuccessful) implements Response {
    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }
}
