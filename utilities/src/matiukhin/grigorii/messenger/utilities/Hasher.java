package matiukhin.grigorii.messenger.utilities;

public class Hasher {
    public static String getConversationHash(String member1, String member2) {
        return String.valueOf(member1.hashCode() + member2.hashCode());
    }
}
