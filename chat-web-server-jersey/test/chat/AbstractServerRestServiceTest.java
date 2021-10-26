package chat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  UserResourceTest.class,
  SessionResourceTest.class,
  SubWebpageResourceTest.class,
  ContactResourceTest.class,
  ConversationResourceTest.class,
  SSE2MessageResourceTest.class
})

public abstract class AbstractServerRestServiceTest {

}
