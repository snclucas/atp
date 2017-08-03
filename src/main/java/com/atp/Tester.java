package com.atp;

import com.atp.scan.EquityScanner;


public class Tester {

	public Tester() {
		
	    EquityScanner scanner = new EquityScanner();
	    scanner.scan();
	}


	

	public static void main(String[] args) {
		new Tester();
	}

}
