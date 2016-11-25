package com.richard.akka.spring;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.richard.akka.spring.DoubleCountingActor.DoubleCountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import static com.richard.akka.spring.SpringExtension.SpringExtProvider;

@Component("countingActor")
@Scope("prototype")
public class CountingActor extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private int count = 0;

    @Autowired
    private CountingActor(ActorSystem actorSystem, AtomicLong actorIdCounter, CountingService countingService) {
        final ActorRef doubleCountingActor = actorSystem.actorOf(
                SpringExtProvider.get(actorSystem).props("doubleCountingActor"),
                "double-counter-" + actorIdCounter.incrementAndGet());

        receive(ReceiveBuilder
                .match(Count.class, countMessage -> {
                            count = countingService.increment(count);
                            log.info("Received Count: {}", count);
                            doubleCountingActor.tell(DoubleCountingActor.DoubleCountProps(count, self().path()), self());
                        }
                )
                .match(DoubleCountResponse.class, doubleCountResponse -> {
                    log.info("Received Double Count: {}", doubleCountResponse.getCount());
                    //context().stop(self());
                })
                .match(Get.class, get -> {
                    sender().tell(count, self());
                    context().stop(self());
                }).matchAny(this::unhandled).build());
    }

    static class Count implements Serializable {
    }

    static class Get implements Serializable {
    }
}
