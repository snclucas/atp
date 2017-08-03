package com.atp.scan;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;

public abstract class AbstractScan {
	
	protected String name;
	protected List<PriceBar> priceBars;
	protected boolean justLastDay = true;
	
	public AbstractScan(String name) {
		this(name, true);
	}
	
	public AbstractScan(String name, boolean justLastDay) {
		this.name = name;
		this.justLastDay = justLastDay;
		priceBars = new ArrayList<PriceBar>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract ScanResult tick(PriceBar priceBar);
	
	public void reset() {
		priceBars.clear();
	}
}
