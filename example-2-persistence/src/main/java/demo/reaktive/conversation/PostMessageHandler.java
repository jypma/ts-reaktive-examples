package demo.reaktive.conversation;

import akka.Done;
import demo.reaktive.conversation.ConversationCommand.PostMessage;
import javaslang.collection.Seq;
import javaslang.collection.Vector;

public class PostMessageHandler extends ConversationActor.Handler {
    public PostMessageHandler(ConversationState state, ConversationCommand cmd) {
        super(state, cmd);
    }

    @Override
    public Seq<ConversationEvent> getEventsToEmit() {
        return Vector.of(ConversationEvent.messagePosted(PostMessage.class.cast(cmd).getMessage()));
    }

    @Override
    public Object getReply(Seq<ConversationEvent> emittedEvents, long lastSequenceNr) {
        return Done.getInstance();
    }

}
