package demo.reaktive;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.persistence.cassandra.query.javadsl.CassandraReadJournal;
import akka.persistence.query.PersistenceQuery;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import demo.reaktive.conversation.ConversationActor;
import demo.reaktive.conversation.ConversationBot;
import demo.reaktive.conversation.ConversationProtocol;
import demo.reaktive.rest.RootRoute;

public class Main {
    public Main(int clusterIndex) {
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + (2551 + clusterIndex)).withFallback(ConfigFactory.load());
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        Materializer materializer = ActorMaterializer.create(system);
        ConversationProtocol conversationProtocol = new ConversationProtocol(system);
        RootRoute rootRoute = new RootRoute(conversationProtocol);
        CassandraReadJournal journal = PersistenceQuery.get(system).getReadJournalFor(CassandraReadJournal.class, CassandraReadJournal.Identifier());
        system.actorOf(Props.create(ConversationBot.class, () ->
            new ConversationBot(journal, conversationProtocol, ConversationActor.sharding())));
        
        Http.get(system).bindAndHandle(
            rootRoute.http().flow(system, materializer),
            ConnectHttp.toHost("127.0.0.1", 8173 + clusterIndex),
            materializer);
    }
    
    public static void main(String[] args) {
        int clusterIndex = (args.length == 0) ? 0 : Integer.parseInt(args[0]);
        new Main(clusterIndex);
    }
}
