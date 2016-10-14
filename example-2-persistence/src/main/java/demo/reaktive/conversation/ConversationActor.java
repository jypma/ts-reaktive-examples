package demo.reaktive.conversation;

import com.tradeshift.reaktive.actors.AbstractCommandHandler;
import com.tradeshift.reaktive.actors.AbstractStatefulPersistentActor;

import akka.japi.pf.PFBuilder;
import demo.reaktive.conversation.ConversationCommand.GetMessageList;
import demo.reaktive.conversation.ConversationCommand.PostMessage;
import scala.PartialFunction;

/**
 * Manages the state for a single conversation
 */
public class ConversationActor extends AbstractStatefulPersistentActor<ConversationCommand, ConversationEvent, ConversationState> {
    public ConversationActor() {
        super(ConversationCommand.class, ConversationEvent.class);
    }

    static abstract class Handler extends AbstractCommandHandler<ConversationCommand, ConversationEvent, ConversationState> {
        public Handler(ConversationState state, ConversationCommand cmd) {
            super(state, cmd);
        }
    }
    
    @Override
    protected PartialFunction<ConversationCommand, Handler> applyCommand() {
        return new PFBuilder<ConversationCommand,Handler>()
            .match(GetMessageList.class, msg -> new GetMessageListHandler(getState(), msg))
            .match(PostMessage.class, msg -> new PostMessageHandler(getState(), msg))
            .build();
    }

    @Override
    protected ConversationState initialState() {
        return ConversationState.EMPTY;
    }
}
