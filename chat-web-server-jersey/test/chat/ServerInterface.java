package chat;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.spi.TestContainerFactory;

public interface ServerInterface {

    public Application getResourceConfig( );
    
    public TestContainerFactory getTestContainerFactory();
}
