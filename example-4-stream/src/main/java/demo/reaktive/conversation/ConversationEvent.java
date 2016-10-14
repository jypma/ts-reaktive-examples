package demo.reaktive.conversation;

import java.io.Serializable;

public abstract class ConversationEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static class MessagePosted extends ConversationEvent {
        private static final long serialVersionUID = 1L;
        
        private final String message;

        public MessagePosted(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
