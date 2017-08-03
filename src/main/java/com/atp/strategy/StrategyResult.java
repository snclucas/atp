package com.atp.strategy;

import java.time.LocalDateTime;

public class StrategyResult {
		
	private LocalDateTime dateTime;
	private double result;
	
	
	public StrategyResult(LocalDateTime dateTime, double result) {
		super();
		this.dateTime = dateTime;
		this.result = result;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public double getResult() {
		return result;
	}
	
}
