package chat.request;

import java.util.List;

public class NewConversationRequest {
    
    private List<Integer> ids;
    
    public NewConversationRequest( List<Integer> ids ) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

}
