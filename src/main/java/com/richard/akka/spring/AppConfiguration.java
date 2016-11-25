package com.richard.akka.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import static SpringExtension.SpringExtProvider;

import akka.actor.ActorSystem;

@Configuration
public class AppConfiguration {
	  @Autowired
	  private ApplicationContext applicationContext;

	  /**
	   * Actor system singleton for this application.
	   */
	  @Bean
	  public ActorSystem actorSystem() {
	    ActorSystem system = ActorSystem.create("AkkaJavaSpring");
	    // initialize the application context in the Akka Spring Extension
	    SpringExtension.SpringExtProvider.get(system).initialize(applicationContext);
	    return system;
	  }
}
