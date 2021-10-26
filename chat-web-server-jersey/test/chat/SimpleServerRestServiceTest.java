package chat;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.simple.SimpleTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.BeforeClass;

public class SimpleServerRestServiceTest extends AbstractServerRestServiceTest implements ServerInterface {
    
    @BeforeClass
    public static void setup() {
        ServerContext.getInstance().setServer( new SimpleServerRestServiceTest() );
    }
    
    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new SimpleTestContainerFactory();
    }

    @Override
    public Application getResourceConfig() {
        return SimpleWebServer.getResourceConfig();
    }
    
}
