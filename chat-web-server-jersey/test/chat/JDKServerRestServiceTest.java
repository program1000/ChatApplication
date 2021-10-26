package chat;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.jdkhttp.JdkHttpServerTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.BeforeClass;

public class JDKServerRestServiceTest extends AbstractServerRestServiceTest implements ServerInterface {
    
    @BeforeClass
    public static void setup() {
        ServerContext.getInstance().setServer( new JDKServerRestServiceTest() );
    }
    
    @Override
    public Application getResourceConfig() {
        return PureJax.getResourceConfig();
    }
    
    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new JdkHttpServerTestContainerFactory();
    }
    

}
