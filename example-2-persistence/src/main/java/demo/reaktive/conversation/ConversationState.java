package demo.reaktive.conversation;

import com.tradeshift.reaktive.actors.AbstractState;

import demo.reaktive.conversation.ConversationEvent.MessagePosted;
import javaslang.collection.Seq;
import javaslang.collection.Vector;

/** Class covering the state that is kept in memory for a conversation */
public class ConversationState extends AbstractState<ConversationEvent, ConversationState> {
    public static final ConversationState EMPTY = new ConversationState(Vector.empty());
    
    private final Seq<String> messages;
    
    private ConversationState(Seq<String> messages) {
        this.messages = messages;
    }

    @Override
    public ConversationState apply(ConversationEvent event) {
        if (event instanceof MessagePosted) {
            return new ConversationState(messages.append(MessagePosted.class.cast(event).getMessage()));
        } else {
            return this;
        }
    }

    public Seq<String> getMessages() {
        return messages;
    }
}
