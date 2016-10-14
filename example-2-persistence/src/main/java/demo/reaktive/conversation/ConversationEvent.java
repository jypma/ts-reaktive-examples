package demo.reaktive.conversation;

import java.io.Serializable;

/** Base class for all events that can occur in a conversation */
public abstract class ConversationEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final class MessagePosted extends ConversationEvent {
        private static final long serialVersionUID = 1L;
        private final String message;
        
        private MessagePosted(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /** Create a new MessagePosted event, indicating that a message has been posted to a conversation */
    public static MessagePosted messagePosted(String message) {
        return new MessagePosted(message);
    }
}
