package demo.reaktive.conversation;

import com.tradeshift.reaktive.actors.AbstractCommandHandler;
import com.tradeshift.reaktive.actors.AbstractStatefulPersistentActor;
import com.tradeshift.reaktive.actors.PersistentActorSharding;

import akka.japi.pf.PFBuilder;
import scala.PartialFunction;

/**
 * Manages the state for a single conversation
 */
public class ConversationActor extends AbstractStatefulPersistentActor<ConversationCommand, ConversationEvent, ConversationState> {
    public static PersistentActorSharding<ConversationCommand> sharding() {
        return PersistentActorSharding.of(ConversationActor.class, "conversation", ConversationCommand::getConversationId);
    }
    
    public static abstract class Handler extends AbstractCommandHandler<ConversationCommand, ConversationEvent, ConversationState> {
        public Handler(ConversationState state, ConversationCommand cmd) {
            super(state, cmd);
        }
    }
    
    public ConversationActor() {
        super(ConversationCommand.class, ConversationEvent.class);
    }

    @Override
    protected ConversationState initialState() {
        return ConversationState.EMPTY;
    }
    
    @Override
    protected PartialFunction<ConversationCommand,Handler> applyCommand() {
        return new PFBuilder<ConversationCommand,Handler>()
            .match(ConversationCommand.GetMessageList.class, cmd -> new GetMessageListHandler(getState(), cmd))
            .match(ConversationCommand.PostMessage.class, cmd -> new PostMessageHandler(getState(), cmd))
            .build();
    }
}
