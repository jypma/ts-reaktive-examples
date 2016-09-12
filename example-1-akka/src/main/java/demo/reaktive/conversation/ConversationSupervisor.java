package demo.reaktive.conversation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Maintains a pool of child actors, one for each converation ID.
 */
public class ConversationSupervisor extends AbstractActor {
    {
        receive(ReceiveBuilder
            .match(RoutedMessage.class, this::route)
            .build()
        );
    }
    
    private void route(RoutedMessage msg) {
        ActorRef child = getContext().getChild(msg.conversationId);
        if (child == null) {
            child = getContext().actorOf(Props.create(ConversationActor.class), msg.conversationId);
        }
        child.forward(msg.payload, getContext());
    }
    
    /**
     * Wrapper for messages that will be routed to child actors
     */
    public static class RoutedMessage {
        private final String conversationId;
        private final Object payload;
        
        public RoutedMessage(String conversationId, Object payload) {
            this.conversationId = conversationId;
            this.payload = payload;
        }
    }
}
