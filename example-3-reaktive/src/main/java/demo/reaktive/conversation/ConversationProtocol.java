package demo.reaktive.conversation;

import static akka.pattern.PatternsCS.ask;

import java.util.concurrent.CompletionStage;

import akka.Done;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import javaslang.collection.Seq;

/**
 * Future-based API into the conversation implementation
 */
public class ConversationProtocol {
    private final ActorRef supervisor;
    
    public ConversationProtocol(ActorSystem system) {
        this.supervisor = system.actorOf(Props.create(ConversationSupervisor.class));
    }

    public CompletionStage<Done> postMessage(String conversationId, String message) {
        return ask(supervisor, new ConversationCommand.PostMessage(conversationId, message), 5000).thenApply(Done.class::cast);
    }

    @SuppressWarnings("unchecked")
    public CompletionStage<Seq<String>> getMessages(String conversationId) {
        return ask(supervisor, new ConversationCommand.GetMessageList(conversationId), 5000).thenApply(l -> (Seq<String>) l);
    }
}
