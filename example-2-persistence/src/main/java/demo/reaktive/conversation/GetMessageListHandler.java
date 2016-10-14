package demo.reaktive.conversation;

import javaslang.collection.Seq;
import javaslang.collection.Vector;

/**
 * Handler for "GetMessageList" commands
 */
public class GetMessageListHandler extends ConversationActor.Handler {

    public GetMessageListHandler(ConversationState state, ConversationCommand cmd) {
        super(state, cmd);
    }

    @Override
    public Seq<ConversationEvent> getEventsToEmit() {
        return Vector.empty();
    }

    @Override
    public Object getReply(Seq<ConversationEvent> emittedEvents, long lastSequenceNr) {
        return state.getMessages();
    }

}
