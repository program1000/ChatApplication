package chat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class JaxRsApplication extends Application {
    private final Set<Class<?>> classes;

    public JaxRsApplication() {
        System.out.println("JAX APP INITED");
        HashSet<Class<?>> c = new HashSet<Class<?>>();
        c.add( HelloResource.class );
        classes = Collections.unmodifiableSet( c );
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
