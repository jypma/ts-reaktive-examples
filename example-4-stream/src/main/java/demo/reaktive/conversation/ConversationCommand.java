package demo.reaktive.conversation;

import java.io.Serializable;

public abstract class ConversationCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String conversationId;
    
    public ConversationCommand(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public static class GetMessageList extends ConversationCommand {
        private static final long serialVersionUID = 1L;
        
        public GetMessageList(String conversationId) {
            super(conversationId);
        }
    }
    
    public static class PostMessage extends ConversationCommand {
        private static final long serialVersionUID = 1L;
        private final String message;
        
        public PostMessage(String conversationId, String message) {
            super(conversationId);
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
