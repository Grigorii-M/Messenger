package matiukhin.grigorii.messenger.utilities.communication.messages;

import matiukhin.grigorii.messenger.utilities.communication.responses.Response;

import java.io.Serializable;

public interface Message extends Serializable {
    Response accept(MessageVisitor visitor);
}
