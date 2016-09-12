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
            .match(ConversationCommand.class, this::route)
            .build()
        );
    }
    
    private void route(ConversationCommand msg) {
        ActorRef child = getContext().getChild(msg.getConversationId());
        if (child == null) {
            child = getContext().actorOf(Props.create(ConversationActor.class), msg.getConversationId());
        }
        child.forward(msg, getContext());
    }
}
