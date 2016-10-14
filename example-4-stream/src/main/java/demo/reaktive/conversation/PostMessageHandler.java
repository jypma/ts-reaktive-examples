package demo.reaktive.conversation;

import akka.Done;
import javaslang.collection.Seq;
import javaslang.collection.Vector;

public class PostMessageHandler extends ConversationActor.Handler {
    public PostMessageHandler(ConversationState state, ConversationCommand.PostMessage cmd) {
        super(state, cmd);
    }

    @Override
    public Seq<ConversationEvent> getEventsToEmit() {
        return Vector.of(new ConversationEvent.MessagePosted(ConversationCommand.PostMessage.class.cast(cmd).getMessage()));
    }

    @Override
    public Object getReply(Seq<ConversationEvent> emittedEvents, long lastSequenceNr) {
        return Done.getInstance();
    }

}
