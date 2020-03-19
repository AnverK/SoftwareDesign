import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.PrintWriter;


public class RequestHandler {
    private PrintWriter out;

    public RequestHandler(PrintWriter out) {
        this.out = out;
    }

    public void getResponses(String query) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef master = system.actorOf(Props.create(MasterActor.class, out, query));
        master.tell("/start", ActorRef.noSender());
    }
}