package com.richard.akka.spring;

import org.springframework.stereotype.Component;

@Component("countingService")
public class CountingService {
	 /**
	   * Increment the given number by one.
	   */
	  public int increment(int count) {
	    return ++count;
	  }
}
