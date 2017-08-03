package com.atp.portfolio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.atp.logging.Message;
import com.atp.logging.Message.MessageType;
import com.atp.logging.MessageWriter;
import com.atp.trade.Trade;


public class Portfolio implements MessageWriter {

	private String name;
	private String ID;
	private double cash;
	private double initialCash;
	private double cashPerTrade;
	private Map<String, Position> positions;
	
	private boolean usePercentOfCapital = false;
	private double percentOfCapital = 0.02;

	public Portfolio(String name, double initialCash) {
		this.name = name;
		positions = new HashMap<String, Position>();
		this.cash = initialCash;
		this.initialCash = initialCash;
		this.cashPerTrade = 0;
		this.ID = UUID.randomUUID().toString();
	}

	public Portfolio(String name, double initialCash, int allowedTrades, double cashPerTrade) {
		this.name = name;
		positions = new HashMap<String, Position>();
		this.cash = initialCash;
		this.initialCash = initialCash;
		this.cashPerTrade = cashPerTrade;
		this.ID = UUID.randomUUID().toString();
	}


	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getUniqueID() {
		return ID;
	}
	

	/**
	 * Return the initial cash value of the com.atp.com.atp.portfolio.
	 * @return double initialCash
	 */
	public double getInitialCash() {
		return initialCash;
	}

	public void setInitialCash(double initialCash) {
		this.initialCash = initialCash;
	}



	public double getPercentOfCapital() {
		return percentOfCapital;
	}

	public void setPercentOfCapital(double percentOfCapital) {
		this.percentOfCapital = percentOfCapital;
	}

	public boolean isUsePercentOfCapital() {
		return usePercentOfCapital;
	}

	public void setUsePercentOfCapital(boolean usePercentOfCapital) {
		this.usePercentOfCapital = usePercentOfCapital;
	}



	public double getCashPerTrade() {
		if(usePercentOfCapital)
			return cash*getPercentOfCapital();
		return cashPerTrade;
	}

	public void setCashPerTrade(double cashPerTrade) {
		this.cashPerTrade = cashPerTrade;
	}

	
	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	


	public Map<String, Position> getAllPositions() {
		return positions;
	}
	
	public int getNumPositions(String symbol) {
		return getPositionsWithSymbol(symbol).size();
	}
	
	public int getNumPositions() {
		return getAllPositions().size();
	}
	

	
	public List<Position> getPositionsWithSymbol(String symbol) {
		List<Position> returnPositions = new ArrayList<Position>();
		
		for(Position position : positions.values()) {
			if(position.getSymbol().equalsIgnoreCase(symbol))
				returnPositions.add(position);
		}
		return returnPositions;
	}
	

	public Position getPosition(String uniqueID) {
		return positions.get(uniqueID);
	}
	
	
	
	public Message addTrade(Trade trade) {
		double costOfTrade = trade.getTradeCost();
		for(Position position : positions.values()) {
			if(position.getSecurity().equals(trade.getSecurity())) {
				 position.addTrade(trade);
				 cash += costOfTrade;
				 return new Message(MessageType.SUCCESS, LocalDateTime.now(), 
						 "Trade added to exisiting position [" + position.getUniqueID() + "]");
			}
		}
		
		// We don't have a position in this security so create a new position
		Position position = new Position(trade);
		positions.put(position.getUniqueID(), position);
		cash += costOfTrade;
		return new Message(MessageType.SUCCESS, LocalDateTime.now(), 
				 "Trade added to new position [" + position.getUniqueID() + "]");
	}
	
	
	public boolean addPosition(Position position) {
		if(!positions.containsKey(position.getUniqueID())) {
			positions.put(position.getUniqueID(), position);
			return true;
		}
		return false;
	}

	
	public boolean removePosition(String uniqueID) {
		if(positions.containsKey(uniqueID)) {
			positions.remove(uniqueID);
			return true;
		}
		return false;
	}
	
	/*
	public int removePosition(String symbol) {
		int numDeleted = 0;
		List<String> delPositions = new ArrayList<String>();
		
		for(String key : positions.keySet()) {
			String symbolPart = key.substring(0, key.indexOf("_"));
			
			if(symbolPart.equalsIgnoreCase(symbol))
				delPositions.add(key);
		}
		//Now delete them
		for(String s : delPositions) {
			positions.remove(s);
			numDeleted++;
		}
		return numDeleted;
	}
	*/
	
	
	public int numPositions() {
		return positions.size();
	}


	public void reset() {
		positions.clear();
		this.cash = this.initialCash;
	}

	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Portfolio other = (Portfolio) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	


}
