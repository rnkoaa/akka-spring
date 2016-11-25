package com.richard.akka.spring;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Component("doubleCountingActor")
@Scope("prototype")
public class DoubleCountingActor extends AbstractLoggingActor {

    public static DoubleCount DoubleCountProps(int count, ActorPath path) {
        return new DoubleCount(count, path);
    }

    static final class DoubleCount implements Serializable {

        private final int count;
        private final ActorPath path;

        DoubleCount(int count, ActorPath path) {
            this.count = count;
            this.path = path;
        }

        public int getCount() {
            return count;
        }

        public ActorPath getPath() {
            return path;
        }
    }

    static final class DoubleCountResponse implements Serializable {

        private final int count;

        DoubleCountResponse(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public DoubleCountingActor() {
        receive(ReceiveBuilder
                .match(DoubleCount.class, doubleCount -> {
                    int count = doubleCount.getCount() * 2;
                    log().info("Doubling the count from: {} to Count: {}",
                            doubleCount.getCount(), count);

                    ActorSelection selection = context().actorSelection(doubleCount.getPath());
                    selection.tell(new DoubleCountResponse(count), ActorRef.noSender());
                  //  context().stop(self());
                })
                .matchAny(this::unhandled)
                .build());
    }
}
