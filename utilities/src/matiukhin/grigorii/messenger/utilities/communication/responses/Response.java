package matiukhin.grigorii.messenger.utilities.communication.responses;

import java.io.Serializable;

public interface Response extends Serializable {
    void accept(ResponseVisitor visitor);
}
