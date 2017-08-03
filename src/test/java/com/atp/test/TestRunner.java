package com.atp.test;

import com.atp.portfolio.PortfolioTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestRunner   {

	public void testStockTrade() {
		
		
	}

	public static Test suite() {
		
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(PortfolioTest.class);
		
		return suite;
	}
	
	/**
     * Runs the com.atp.test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

}
