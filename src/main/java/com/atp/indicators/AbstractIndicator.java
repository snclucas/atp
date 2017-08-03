package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all classes implementing technical com.atp.indicators.
 */
public abstract class AbstractIndicator implements Indicator {

	protected double value;
	protected String name;
	protected List<Double> historyVals;
	protected boolean saveHistory;
	int historyindowLength = 100;

	
	public AbstractIndicator(String name, boolean saveHistory) {
		this.name = name;
		this.saveHistory = saveHistory;
		historyVals = new ArrayList<Double>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getHistoryWindow() {
		return historyindowLength;
	}


	public void HistoryWindow(int historyindowLength) {
		this.historyindowLength = historyindowLength;
	}


	public void saveHistoryValue(double value) {
		if(saveHistory) {
			if(historyVals.size()>historyindowLength) 
				historyVals.remove(0);
			historyVals.add(value);
		}
	}


	public double getHistoryValue(int i) {
		return historyVals.get(historyVals.size()-1 - i);
	}


}
