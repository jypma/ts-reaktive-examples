package demo.reaktive.rest;

import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.completeOK;
import static akka.http.javadsl.server.Directives.entity;
import static akka.http.javadsl.server.Directives.get;
import static akka.http.javadsl.server.Directives.getFromResourceDirectory;
import static akka.http.javadsl.server.Directives.onSuccess;
import static akka.http.javadsl.server.Directives.pathEndOrSingleSlash;
import static akka.http.javadsl.server.Directives.pathPrefix;
import static akka.http.javadsl.server.Directives.post;
import static akka.http.javadsl.server.Directives.redirect;
import static akka.http.javadsl.server.Directives.route;
import static com.tradeshift.reaktive.json.JSONProtocol.array;
import static com.tradeshift.reaktive.json.JSONProtocol.stringValue;
import static com.tradeshift.reaktive.marshal.Protocol.seq;

import com.tradeshift.reaktive.json.JSONEvent;
import com.tradeshift.reaktive.marshal.WriteProtocol;
import com.tradeshift.reaktive.marshal.http.JSONMarshallers;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import demo.reaktive.conversation.ConversationProtocol;
import javaslang.collection.Seq;

public class RootRoute {
    private final ConversationProtocol conversations;
    
    public RootRoute(ConversationProtocol conversations) {
        this.conversations = conversations;
    }
    
    /**
     * Returns the root route that should be available over HTTP.
     */
    public Route http() {
        return route(
            pathEndOrSingleSlash(() ->
                redirect(Uri.create("index.html"), StatusCodes.PERMANENT_REDIRECT)
            ),
            pathPrefix("conversations", () ->
                conversations()
            ),
            getFromResourceDirectory("http")
        );
    }
    
    private static final WriteProtocol<JSONEvent, Seq<String>> protocol = array(seq(stringValue));
    
    private Route conversations() {
        return pathPrefix(conversationId ->
            route(
                post(() ->
                    entity(Unmarshaller.entityToString(), message ->
                        onSuccess(() -> conversations.postMessage(conversationId, message), done ->
                            complete(StatusCodes.CREATED)
                        )
                    )
                ),
                get(() ->
                    onSuccess(() -> conversations.getMessages(conversationId), msgs ->
                        completeOK(msgs, JSONMarshallers.toJSON(protocol))
                    )
                )
            )
        );
    }
}
