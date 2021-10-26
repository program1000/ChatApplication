package chat.connector;

public class ConnectorRegister {

    private static AccountConnector accountConnector;
    private static ContactConnector contactConnector;
    private static MessageServerConnector messageServerConnector;
    
    public static void setAccountConnector( AccountConnector connector ) {
        accountConnector = connector;
    }

    public static AccountConnector getAccountConnector() {
        return accountConnector;
    }
    
    public static void setContactConnector( ContactConnector connector ) {
        contactConnector = connector;
    }

    public static ContactConnector getContactConnector() {
        return contactConnector;
    }
    
    public static void setMessageServerConnector( MessageServerConnector connector ) {
        messageServerConnector = connector;
    }
    
    public static MessageServerConnector getMessageServerConnector() {
        return messageServerConnector;
    }
}
