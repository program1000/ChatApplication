package chat;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;

import chat.connector.SecurityManagerStub;

public class AbstractResource extends JerseyTest{
    
    private static ServerInterface server = ServerContext.getInstance().getServer();
    
    protected SecurityManagerStub securityManager;
    
    //every @test
    @Before
    public void setup() {
        securityManager = new SecurityManagerStub();
        SecurityManager.set( securityManager );
        
        setupConnectors();
        
        client().register(JacksonFeature.class);
        additionalClientSetup( client() );
    }
    
    protected void setupConnectors() {
      //empty default
    }
    
    protected void additionalClientSetup( Client client) {
        //empty default
    }
    
    @Override
    protected Application configure() {
        return server.getResourceConfig();
    }
    
    @Override
    public TestContainerFactory getTestContainerFactory() {
        return server.getTestContainerFactory();
    }

}
