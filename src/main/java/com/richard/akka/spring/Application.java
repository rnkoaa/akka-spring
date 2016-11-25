package com.richard.akka.spring;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static com.richard.akka.spring.SpringExtension.SpringExtProvider;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private CountingService countingService;

    @Bean(name = "actorIdCounter")
    public AtomicLong actorIdCounter() {
        return new AtomicLong(0);
    }

    /*@Bean
    @Scope("prototype")
    ActorRef countingActor(ActorRef doubleCountingActor, CountingService countingService) {
        *//*return actorSystem
                .actorOf(CountingActor.props(doubleCountingActor, countingService),
                        "counter-" + actorIdCounter.incrementAndGet());*//*
       *//* final ActorRef myActor = actorSystem.actorOf(
                Props.create(SpringActorProducer.class, applicationContext, "counting"),
                "myactor3");*//*
    }*/


   /* @Bean
    @Scope("prototype")
    ActorRef doubleCountingActor() {
        return actorSystem
                .actorOf(DoubleCountingActor.props(),
                        "double-counter-" + actorIdCounter.incrementAndGet());
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        assert (actorSystem != null);


        IntStream.range(0, 10).forEach((index) -> {
            final ActorRef counter = actorSystem.actorOf(
                    SpringExtProvider.get(actorSystem).props("countingActor"),
                    "countingActor-" + actorIdCounter().incrementAndGet());
            counter.tell(new CountingActor.Count(), ActorRef.noSender());
            counter.tell(new CountingActor.Count(), ActorRef.noSender());
            counter.tell(new CountingActor.Count(), ActorRef.noSender());
            counter.tell(new CountingActor.Count(), ActorRef.noSender());
        });


        // print the result
       /* FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(counter, new CountingActor.Get(), Timeout.durationToTimeout(duration));
		try {
			System.out.println("Got back " + Await.result(result, duration));
		} catch (Exception e) {
			System.err.println("Failed getting result: " + e.getMessage());
			throw e;
		} finally {
			actorSystem.shutdown();
			actorSystem.awaitTermination();
		}*/
    }
}
