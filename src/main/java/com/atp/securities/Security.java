package com.atp.securities;


import com.atp.UniquelyIdentifiable;

public interface Security{
	
	public enum SecurityType { 
		STOCK("Stock"), BOND("Bond"), OPTION("Option");
		private String tag;
		SecurityType(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};

	String getSecurityId();
	SecurityType getType();
	String getSymbol();
	double getPayoff(double currentMarketPrice);
	double getBookCost();

}
