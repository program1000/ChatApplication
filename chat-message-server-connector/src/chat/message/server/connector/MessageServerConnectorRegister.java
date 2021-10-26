package chat.message.server.connector;

public class MessageServerConnectorRegister {
    private static MessageServerAccountConnector accountConnector;
    private static MessageServerConversationConnector conversationConnector;

    public static MessageServerAccountConnector getAccountConnector() {
        return accountConnector;
    }

    public static void setAccountConnector( MessageServerAccountConnector accountConnector ) {
        MessageServerConnectorRegister.accountConnector = accountConnector;
    }

    public static MessageServerConversationConnector getConversationConnector() {
        return conversationConnector;
    }

    public static void setConversationConnector( MessageServerConversationConnector conversationConnector ) {
        MessageServerConnectorRegister.conversationConnector = conversationConnector;
    }
    
    

}
