package demo.reaktive.conversation;

import com.tradeshift.reaktive.actors.AbstractStatefulPersistentActor;
import com.tradeshift.reaktive.actors.PersistentActorSharding;
import com.tradeshift.reaktive.akka.SharedActorMaterializer;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.query.EventEnvelope;
import akka.persistence.query.javadsl.EventsByTagQuery;
import akka.stream.javadsl.Sink;

public class ConversationBot extends AbstractActor {
    public ConversationBot(EventsByTagQuery query, ConversationProtocol protocol, PersistentActorSharding<ConversationCommand> sharding) {
        String tag = AbstractStatefulPersistentActor.getEventTag(context().system().settings().config(), ConversationEvent.class);
        query.eventsByTag(tag, System.currentTimeMillis())
            .filter(this::isBotRequest)
            .mapAsyncUnordered(50, envelope -> {
                String conversationId = sharding.getPersistenceIdPostfix(envelope.persistenceId());
                return protocol.postMessage(conversationId, "Hello, how may I help you?");
            })
            .filter(done -> false)
            .runWith(Sink.actorRef(self(), "done"), SharedActorMaterializer.get(context().system()));
        receive(ReceiveBuilder
            .matchEquals("done", msg -> {
                throw new RuntimeException("Oh no stream completed. This shouldn't happen. TODO auto restart.");
            })
            .build());
    }
    
    private boolean isBotRequest(EventEnvelope envelope) {
        return ConversationEvent.MessagePosted.class.isInstance(envelope.event()) &&
               ConversationEvent.MessagePosted.class.cast(envelope.event()).getMessage().equals("/bot");
    }

}
