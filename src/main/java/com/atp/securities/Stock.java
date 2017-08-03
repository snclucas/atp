package com.atp.securities;

public class Stock extends AbstractSecurity {	
	
	private double bookPrice;
	
	public Stock(String symbol, double bookPrice) {
		super(symbol);
		this.bookPrice = bookPrice;
	}
	
	
	@Override
	public double getBookCost() {
		return bookPrice;
	}
	

	@Override
	public double getPayoff(double currentPrice) {
		return currentPrice - bookPrice;
	}

	@Override
	public SecurityType getType() {
		return SecurityType.STOCK;
	}

}
