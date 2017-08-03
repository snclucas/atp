package com.atp.securities;

import java.time.LocalDate;


public class Option extends AbstractSecurity {	
	
	public enum OptionType { 
		CALL("Call"), PUT("Put");
		private String tag;
		OptionType(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};
	
	private double strike;
	private LocalDate expiry;
	private OptionType optionType;
	private double premium;
	
	public Option(String symbol, OptionType optionType, double strike, LocalDate expiry, double premium) {
		super(symbol);
		this.optionType = optionType;
		this.strike = strike;
		this.expiry = expiry;
		this.premium = premium;
	}
	
	
	@Override
	public double getBookCost() {
		return premium;
	}
	
	
	@Override
	public SecurityType getType() {
		return SecurityType.OPTION;
	}
	

	@Override
	public double getPayoff(double currentPrice) {
		if(optionType == OptionType.CALL)
			return Math.min(currentPrice-strike,0) - premium;
		else 
			return Math.min(currentPrice-strike,0) - premium; //change
	}


	public double getStrike() {
		return strike;
	}

	public LocalDate getExpiry() {
		return expiry;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public double getPremium() {
		return premium;
	}

	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expiry == null) ? 0 : expiry.hashCode());
		result = prime * result
				+ ((optionType == null) ? 0 : optionType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(strike);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Option other = (Option) obj;
		if (expiry == null) {
			if (other.expiry != null)
				return false;
		} else if (!expiry.equals(other.expiry))
			return false;
		if (optionType != other.optionType)
			return false;
		if (Double.doubleToLongBits(strike) != Double
				.doubleToLongBits(other.strike))
			return false;
		return true;
	}

}
