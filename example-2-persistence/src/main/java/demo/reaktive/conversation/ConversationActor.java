package demo.reaktive.conversation;

import java.util.ArrayList;
import java.util.List;

import akka.Done;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Manages the state for a single conversation
 */
public class ConversationActor extends AbstractPersistentActor {
    private final List<String> messages = new ArrayList<>();
    
    @Override
    public PartialFunction<Object, BoxedUnit> receiveCommand() {
        return ReceiveBuilder
            .match(String.class, this::postMessage)
            .match(GetMessageList.class, msg -> getMessageList())
            .build();
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receiveRecover() {
        return ReceiveBuilder
            .match(String.class, messages::add)
            .build();
    }

    private void postMessage(String msg) {
        persist(msg, evt -> {
            messages.add(msg);
            sender().tell(Done.getInstance(), self());
        });
    }
    
    private void getMessageList() {
        sender().tell(new ArrayList<>(messages), self());
    }
    
    public static final class GetMessageList { private GetMessageList() {} }
    /** Message that can be sent to this actor to retrieve the full message list */
    public static final GetMessageList GET_MESSAGE_LIST = new GetMessageList();
    
    @Override
    public String persistenceId() {
        return self().path().name();
    }
}
