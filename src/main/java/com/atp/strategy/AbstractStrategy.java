package com.atp.strategy;


public abstract class AbstractStrategy implements Strategy {
	
	private String name;

	public AbstractStrategy(String name) {
		this.name = name;
	}

	
	public String getName() {
		return name;
	}
	
}
