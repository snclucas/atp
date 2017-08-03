package com.atp.securities;


public abstract class AbstractSecurity implements Security {

	private String symbol;
	
  AbstractSecurity(String symbol) {
		this.symbol = symbol;
	}
	
	
	public String getSymbol() {
		return this.symbol;
	}


	@Override
	public String getSecurityId() {
		return Integer.toString((symbol.toLowerCase().hashCode() + this.getClass().hashCode()));
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSecurity other = (AbstractSecurity) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	

}
