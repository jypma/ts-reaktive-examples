package demo.reaktive.conversation;

import java.util.ArrayList;
import java.util.List;

import akka.Done;
import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

/**
 * Manages the state for a single conversation
 */
public class ConversationActor extends AbstractActor {
    private final List<String> messages = new ArrayList<>();
    
    {
        receive(ReceiveBuilder
            .match(String.class, this::postMessage)
            .match(GetMessageList.class, msg -> getMessageList())
            .build()
        );
    }
    
    private void postMessage(String msg) {
        messages.add(msg);
        sender().tell(Done.getInstance(), self());
    }
    
    private void getMessageList() {
        sender().tell(new ArrayList<>(messages), self());
    }
    
    public static final class GetMessageList { private GetMessageList() {} }
    /** Message that can be sent to this actor to retrieve the full message list */
    public static final GetMessageList GET_MESSAGE_LIST = new GetMessageList();
}
