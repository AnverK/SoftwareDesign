package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.routing.RoundRobinPool;
import responses.CollectedResponse;
import responses.SingleResponse;
import scala.concurrent.duration.Duration;
import searchers.GoogleSearcher;
import searchers.YandexSearcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class MasterActor extends UntypedAbstractActor {
    private final ActorRef childRouter;
    private PrintWriter out;
    private String query;

    private static final int SEARCHERS_NUM = 2;
    private List<CollectedResponse> responses = new ArrayList<>();

    public MasterActor(PrintWriter out, String query) {
        this.out = out;
        this.query = query;
        childRouter = getContext().actorOf(new RoundRobinPool(SEARCHERS_NUM).props(Props.create(ChildActor.class)), "childRouter");
        getContext().setReceiveTimeout(Duration.create("1 second"));
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message.equals("/start")) {
            childRouter.tell(new GoogleSearcher(query), getSelf());
            childRouter.tell(new YandexSearcher(query), getSelf());
        } else if (message instanceof CollectedResponse) {
            responses.add((CollectedResponse) message);
            if (responses.size() == SEARCHERS_NUM) {
                for (CollectedResponse response : responses) {
                    out.println("Got answer from " + response.getName());
                    out.println(response.getResponses().size());
                    for (SingleResponse result : response.getResponses()) {
                        out.println(result);
                    }
                }
//                getContext().stop(getSelf());
                throw new StopException();
            }
        }
    }

    @Override
    public void postStop() {
        System.out.println("Master was stopped");
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(false, DeciderBuilder
                .match(StopException.class, e -> OneForOneStrategy.stop())
                .build());
    }

    public static class StopException extends RuntimeException {
        StopException() {
            super();
        }
    }
}