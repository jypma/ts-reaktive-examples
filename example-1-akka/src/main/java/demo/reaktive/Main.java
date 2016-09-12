package demo.reaktive;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import demo.reaktive.conversation.ConversationProtocol;
import demo.reaktive.rest.RootRoute;

public class Main {
    {
        ActorSystem system = ActorSystem.create();
        Materializer materializer = ActorMaterializer.create(system);
        ConversationProtocol conversationProtocol = new ConversationProtocol(system);
        RootRoute rootRoute = new RootRoute(conversationProtocol);
        
        Http.get(system).bindAndHandle(
            rootRoute.http().flow(system, materializer),
            ConnectHttp.toHost("127.0.0.1",8173),
            materializer);
    }
    
    public static void main(String[] args) {
        new Main();
    }
}
