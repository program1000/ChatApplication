package chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.connector.AccountConnector;
import chat.connector.ConnectorRegister;
import chat.connector.ContactConnector;
import chat.connector.MessageServerConnector;
import chat.db.DbInterface;
import chat.message.model.ConversationMessage;
import chat.message.model.LoginMessage;
import chat.message.server.connector.MessageServerAccountConnector;
import chat.message.server.connector.MessageServerConnectorRegister;
import chat.message.server.connector.MessageServerConversationConnector;
import chat.model.MessageServer;

public class AccountServerApplication {
    
    private enum Type {JDK,SIMPLE};

    public static void main( String[] args ) {
        AccountServerApplication app = new AccountServerApplication();
        app.test( Type.SIMPLE );
        
    }
        
    private void startJDKServer() {
        try {
            PureJax.startServer();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }
    
    private void startSimpleServer() {
        SimpleWebServer.startServer();
    }
    
    private void test( Type serverType ) {
        ZeroConfig zc = new ZeroConfig();
        if( !zc.isInited() ) {
            throw new RuntimeException("Not inited");
        }
        
        AccountConnector accountConnector = new AccountConnector();
        accountConnector.setAccountConnector( zc.getAccountProcessor() );
        ConnectorRegister.setAccountConnector( accountConnector );
        
        ContactConnector contactConnector = new ContactConnector();
        contactConnector.setContactConnector( zc.getContactProcessor() );
        ConnectorRegister.setContactConnector( contactConnector );
        
        MessageServerConnector messageServerConnector = new MessageServerConnector();
        messageServerConnector.setMessageServerConnector( zc.getMessageServerProcessor() );
        ConnectorRegister.setMessageServerConnector( messageServerConnector );        
        
        Map<String, List<MessageServer>> defaultMessageServerMap = new HashMap<String, List<MessageServer>>();
        List<MessageServer> serverList = new ArrayList<MessageServer>();
        serverList.add( new MessageServer( "Default", "localhost" ) );
        defaultMessageServerMap.put( DbInterface.DEFAULT_SERVERS, serverList );
        zc.getDb().setDefaultMessageServers( defaultMessageServerMap );
        
        initMessageServer();
        
        switch( serverType) {
        case JDK:
            startJDKServer();
            break;
        case SIMPLE:
            startSimpleServer();
            break;
        default:
            System.out.println("No web server selected to start !");
            break;
        }
        
        ClientDummy dA = new ClientDummy( 1 );
        String sessionIdA = dA.getUser( "user/new" );
        System.out.println("Sid1: "+ sessionIdA);
        
        ClientDummy dB = new ClientDummy( 2 );
        String sessionIdB = dB.getUser( "user/new" );
        System.out.println("Sid2: "+ sessionIdB);
        
        String inviteA = dA.getInvite( "contact/invite" );
        System.out.println("Invite1: "+ inviteA);
        
        String inviteB = dB.getInvite( "contact/invite" );
        System.out.println("Invite2: "+ inviteB);
        
        dA.login( "session/new");
        
        String addContactA = dA.addContact( "contact/new", inviteB );
        System.out.println("Add Contact (2) to 1: "+ addContactA);
        
        dB.login( "session/new");
        
        String addContactB = dB.addContact( "contact/new", inviteA );
        System.out.println("Add Contact (1) to 2: "+ addContactB);
        
        String messageServerA = dA.getMessageServer( "messageserverindex/default", sessionIdA, "M"+DbInterface.DEFAULT_SERVERS );
        System.out.println("Message server1: "+ messageServerA);
        
        String messageServerB = dB.getMessageServer( "messageserverindex/default", sessionIdB, "M"+DbInterface.DEFAULT_SERVERS );
        System.out.println("Message server2: "+ messageServerB);
        

        List<String> ids = new ArrayList<String>();
        //ids.add( "U1C2" );
        ids.add( "U2C1" );
        ConversationMessage cm = new ConversationMessage( ids );
        
        String convB = dB.sendConv( "conversation/new", cm );
        System.out.println("Conv id: "+ convB);
        
//        String tokenA = dA.sendSSE( "message/request", "" );
//        System.out.println("Token 1: "+ tokenA);
        
        String tokenB = dB.sendSSE( "message/request", "" );
        System.out.println("Token 2: "+ tokenB);
//                
//        dA.registerSSE( "message/register", tokenA );
//        System.out.println("Reg 1");
        
        dB.registerSSE( "message/register", tokenB );
        System.out.println("Reg 2");
        
        String rB = dB.send( "message/new", "1MTES" );
        System.out.println( "Result message from 2: " + rB );
        
//        String cA = dA.send( "message/close", "1" );
//        System.out.println("Close 1: "+ cA);
//        dA.closeSSE();
//        String rB = dB.send( "message/new", "OLD" );
//        System.out.println( "Result message from 2: " + rB );
        
        String clcoB = dB.send( "conversation/close", "" );
        System.out.println("Close conv: "+ clcoB);
        
//        
//        dB.closeSSE();
        
//        String cB = dB.send( "message/close", "2" );
//        System.out.println("Close 2: "+ cB);
        
//        dA.registerSSE( "message/register" );
//        System.out.println("Reg 1");
        
//        dA.closeSSE();
//        cA = dA.send( "message/close", "1" );
//        System.out.println("Close 1: "+ cA);
    }
    
    private void initMessageServer() {
        MessageServerZeroConfig zc = new MessageServerZeroConfig();
        
        MessageServerAccountConnector accountConnector = new MessageServerAccountConnector();
        accountConnector.setAccountConnector( zc.getAccountProcessor() );
        MessageServerConnectorRegister.setAccountConnector( accountConnector );
        
        MessageServerConversationConnector conversationConnector = new MessageServerConversationConnector();
        conversationConnector.setConversationConnector( zc.getConversionProcessor() );
        MessageServerConnectorRegister.setConversationConnector( conversationConnector );
    }
}
