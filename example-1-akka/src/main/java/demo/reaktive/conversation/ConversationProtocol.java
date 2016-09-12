package demo.reaktive.conversation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.Done;

/**
 * Future-based API into the conversation implementation
 */
public class ConversationProtocol {
    public CompletionStage<Done> postMessage(String conversationId, String message) {
        // TODO implement
        System.out.println("Received a message: " + message + " for converation: " + conversationId);
        return CompletableFuture.completedFuture(Done.getInstance());
    }

    public CompletionStage<List<String>> getMessages(String conversationId) {
        // TODO implement
        return CompletableFuture.completedFuture(Arrays.asList("Hello!", "I was here"));
    }
}
