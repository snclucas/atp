package com.atp;

import java.util.Observable;
import java.util.Observer;

import com.atp.scan.Scanner;
import com.atp.strategy.IStrategy;
import com.atp.strategy.Strategy;



public class OldDriver implements Observer{

	public OldDriver() {
	
		Scanner scanner = new Scanner();
		scanner.scan();

	}


	public static void main(String[] args) {
		new OldDriver();
	}

	public void update(Observable o, Object arg) {
		Strategy strat = (Strategy)(o);
		System.err.println(strat.getName() + " " + arg.toString());

	}

}
