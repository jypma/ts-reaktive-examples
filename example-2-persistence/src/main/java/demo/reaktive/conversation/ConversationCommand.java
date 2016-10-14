package demo.reaktive.conversation;

import java.io.Serializable;

/** Base class for commands that can be sent to the actor */
public abstract class ConversationCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final class GetMessageList extends ConversationCommand {
        private static final long serialVersionUID = 1L;
        private GetMessageList() {}
    }
    
    public static final class PostMessage extends ConversationCommand {
        private static final long serialVersionUID = 1L;
        private final String message;

        private PostMessage(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /** Command that can be sent to the actor to retrieve the full message list */
    public static GetMessageList getMessageList() {
        return new GetMessageList();
    }
    
    /** Command that can be sent to the actor to request posting a new entry to the conversation */
    public static PostMessage postMessage(String message) {
        return new PostMessage(message);
    }
}
