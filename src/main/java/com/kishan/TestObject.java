package com.kishan;

public class TestObject {
	
	private String greetings;
	
	private long counter;
	
	public TestObject(String greetings, long counter) {
		this.greetings=greetings;
		this.counter=counter;
		
	}

	public String getGreetings() {
		return greetings;
	}

	public void setGreetings(String greetings) {
		this.greetings = greetings;
	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}


}
