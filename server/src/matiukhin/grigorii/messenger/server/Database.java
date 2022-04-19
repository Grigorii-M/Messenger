package matiukhin.grigorii.messenger.server;

import com.google.gson.*;
import matiukhin.grigorii.messenger.server.tcp_connection.Session;
import matiukhin.grigorii.messenger.utilities.Hasher;
import matiukhin.grigorii.messenger.utilities.communication.TextMessage;
import matiukhin.grigorii.messenger.utilities.communication.responses.NewConversationResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class Database {
    enum JsonProperties {
        PASSWORD("password"),
        CONVERSATIONS("conversations"),
        CONVERSATION_MEMBERS("members"),
        CONVERSATION_MESSAGES("messages"),
        MESSAGE_SENDER("sender"),
        MESSAGE_RECEIVER("receiver"),
        MESSAGE_CONTENT("content");

        private final String propertyName;

        JsonProperties(String propertyName) {
            this.propertyName = propertyName;
        }
    }

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    private final String USERS_FILE_PATH = "./users.json";
    private final String CONVERSATIONS_FILE_PATH = "./conversations.json";

    private final JsonObject users;
    private final JsonObject conversations;
    private final HashMap<String, Session> onlineUsers;

    private Database() {
        JsonObject users1;
        JsonObject conversations1;
        try {
            users1 = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH))), JsonObject.class);
            conversations1 = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(CONVERSATIONS_FILE_PATH))), JsonObject.class);

            if (users1 == null) {
                users1 = new JsonObject();
            }

            if (conversations1 == null) {
                conversations1 = new JsonObject();
            }
        } catch (IOException e) {
            try {
                new File(USERS_FILE_PATH).createNewFile();
                new File(CONVERSATIONS_FILE_PATH).createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            users1 = new JsonObject();
            conversations1 = new JsonObject();
        }

        users = users1;
        conversations = conversations1;
        onlineUsers = new HashMap<>();
    }

    private void updateUsersFile() {
        File usersFile = new File(USERS_FILE_PATH);

        try (FileWriter usersFileWriter = new FileWriter(usersFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            usersFileWriter.write(gson.toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateConversationsFile() {
        File conversationsFile = new File(CONVERSATIONS_FILE_PATH);

        try (FileWriter conversationsFileWriter = new FileWriter(conversationsFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            conversationsFileWriter.write(gson.toJson(conversations));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateLogin(String username, String password, Session session) {
        System.out.println(users.get(username).getAsJsonObject().has("password"));
        if (users.has(username)) {
            if (users.get(username).getAsJsonObject().get(JsonProperties.PASSWORD.propertyName).getAsString().equals(password)) {
                onlineUsers.put(username, session);
                System.out.println(onlineUsers);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean addNewUser(String username, String password) {
        JsonObject user = new JsonObject();
        user.add(JsonProperties.PASSWORD.propertyName, new JsonPrimitive(password));
        user.add(JsonProperties.CONVERSATIONS.propertyName, new JsonArray());

        if (users.has(username)) {
            return false;
        } else {
            users.add(username, user);
            updateUsersFile();
            return true;
        }
    }

    private boolean conversationExists(String member1, String member2) {
        return conversations.has(Hasher.getConversationHash(member1, member2));
    }

    public boolean createNewConversation(String sender, String target) {
        if (users.has(sender) && users.has(target) && !sender.equals(target) && !conversationExists(sender, target)) {
            JsonObject conversation = new JsonObject();
            JsonArray members = new JsonArray();
            members.add(new JsonPrimitive(sender));
            members.add(new JsonPrimitive(target));
            conversation.add(JsonProperties.CONVERSATION_MEMBERS.propertyName, members);
            conversation.add(JsonProperties.CONVERSATION_MESSAGES.propertyName, new JsonArray());

            int conversationHashCode = sender.hashCode() + target.hashCode();
            conversations.add(String.valueOf(conversationHashCode), conversation);
            addConversationToUser(conversationHashCode, sender);
            addConversationToUser(conversationHashCode, target);
            updateConversationsFile();

            return true;
        } else {
            return false;
        }
    }

    private void addConversationToUser(int conversationHashCode, String userName) {
        if (users.has(userName)) {
            JsonObject user = users.get(userName).getAsJsonObject();
            user.get(JsonProperties.CONVERSATIONS.propertyName).getAsJsonArray().add(conversationHashCode);
            updateUsersFile();
        }
    }

    public void addNewMessage(String sender, String target, String content) {
        JsonObject message = new JsonObject();
        message.add(JsonProperties.MESSAGE_SENDER.propertyName, new JsonPrimitive(sender));
        message.add(JsonProperties.MESSAGE_RECEIVER.propertyName, new JsonPrimitive(target));
        message.add(JsonProperties.MESSAGE_CONTENT.propertyName, new JsonPrimitive(content));

        JsonObject conversation = conversations.get(String.valueOf(sender.hashCode() + target.hashCode())).getAsJsonObject();
        conversation.get(JsonProperties.CONVERSATION_MESSAGES.propertyName).getAsJsonArray().add(message);
        updateConversationsFile();

        if (onlineUsers.containsKey(target)) {
            onlineUsers.get(target).addToOutputQueue(new TextMessage(sender, content, target));
        }
    }

    public ArrayList<String> getConversationsForUser(String userName) {
        ArrayList<String> data = new ArrayList<>();

        JsonObject user = users.get(userName).getAsJsonObject();
        JsonArray conversationsArray = user.get(JsonProperties.CONVERSATIONS.propertyName).getAsJsonArray();
        for (JsonElement element : conversationsArray) {
            int hash = element.getAsInt();
            JsonObject conversation = conversations.get(String.valueOf(hash)).getAsJsonObject();

            JsonArray members = conversation.get(JsonProperties.CONVERSATION_MEMBERS.propertyName).getAsJsonArray();
            for (JsonElement e : members) {
                String member = e.getAsString();

                if (!member.equals(userName)) {
                    data.add(member);
                }
            }
        }

        return data;
    }

    public ArrayList<TextMessage> getConversationBetween(String sender, String target) {
        ArrayList<TextMessage> data = new ArrayList<>();

        if (conversations.has(Hasher.getConversationHash(sender, target))) {
            JsonArray conversation = conversations.get(Hasher.getConversationHash(sender, target)).getAsJsonObject()
                    .get(JsonProperties.CONVERSATION_MESSAGES.propertyName).getAsJsonArray();
            for (JsonElement e : conversation) {
                JsonObject o = (JsonObject) e;
                data.add(new TextMessage(o.get(JsonProperties.MESSAGE_SENDER.propertyName).getAsString(),
                        o.get(JsonProperties.MESSAGE_CONTENT.propertyName).getAsString(),
                        o.get(JsonProperties.MESSAGE_RECEIVER.propertyName).getAsString()));
            }
        }
        return data;
    }

    public void userLogout(String username) {
        onlineUsers.remove(username);
        System.out.println(onlineUsers);
    }
}
