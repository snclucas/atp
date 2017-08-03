package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;

public class CandleStickPatternMatcher {


	private static  final   float   ignPct      = 5;    // 5%  is ignoring value percentage
	private static  final   float   visPct      = 20;   // 20% is visible value percentage
	private static  final   int     allPct      = 90;   // 90% is about equal to all of it
	private static  final   int     hugeDiff    = 10;   // 10 times is huge difference
	private static  final   int     largeDiff   = 5;    // 5  times is large difference
	private static  final   int     signDiff    = 2;    // 2  times is significant difference

	private List<PriceBar> priceBars; 
	private PriceBar currentBar;
	
	/** Creates a new instance of QuoteCandlePatternImpl */
	public CandleStickPatternMatcher() {
		priceBars = new ArrayList<PriceBar>();
	}
	
	
	public int tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		currentBar = priceBar;

		if(isNormalDay()) System.err.print(" isNormalDay()");
		if(isDoji()) System.err.print(" isDoji()");
		if(isSomeDoji()) System.err.print(" isSomeDoji()");
		if(isShootingStar()) System.err.print(" isShootingStar()");
		if(isSpinningTop() ) System.err.print(" isSpinningTop()");
		if(isDragonflyDoji()) System.err.print(" isGravestoneDoji()");
		if(isGravestoneDoji() ) System.err.print(" isGravestoneDoji()");
		if(isHummer() ) System.err.print(" isHummer()");
		if(isMarubozu()) System.err.print(" isMarubozu()");
		
		return 0;
	}

	public boolean isSomeDoji() {

		// |Open-Close| < (by far) |High-Low| and difference between Open and Close is minimal
		if(currentBar.getOpenClose() * hugeDiff < currentBar.getHighLow() && IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getOpenClose(), ignPct))      {
			return true;
		}
		return false;
	}

	public boolean isDoji() {

		if(isSomeDoji())    {
			// cross doji
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), Math.abs(currentBar.getLowerStick() - currentBar.getUpperStick()), visPct))  {
				return true;
			}
		}
		return false;
	}

	public boolean isDragonflyDoji() {
		if(isSomeDoji())    {
			// Upper shadow
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getUpperStick(), ignPct) && currentBar.getUpperStick() * hugeDiff < currentBar.getLowerStick())  {
				return true;
			}
		}
		return false;
	}

	public boolean isGravestoneDoji() {
		if(isSomeDoji())    {
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getLowerStick(), ignPct) && currentBar.getLowerStick() * hugeDiff < currentBar.getUpperStick())  {
				return true;
			}
		}
		return false;
	}

	public boolean isShootingStar() {
		if(!isGravestoneDoji()) {
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getLowerStick(), ignPct) && currentBar.getOpenClose() * largeDiff < currentBar.getUpperStick()) {
				return true;
			}
		}
		return false;
	}

	public boolean isHummer() {
		if(!isDragonflyDoji())  {
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getUpperStick(), ignPct) && currentBar.getOpenClose() * largeDiff < currentBar.getLowerStick()) {
				return true;
			}
		}
		return false;
	}

	public boolean isMarubozu() {
		if(!isSomeDoji())   {
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getUpperStick(), ignPct) &&
					IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getLowerStick(), ignPct) &&
					IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getOpenClose(), allPct)   )   {
				return true;
			}
		}
		return false;
	}

	public boolean isSpinningTop() {
		if(!isSomeDoji())   {
			if(IndUtil.isValueWithinPct(currentBar.getHighLow(), currentBar.getOpenClose(), visPct) &&
					currentBar.getUpperStick() > currentBar.getOpenClose() * signDiff && currentBar.getLowerStick() > currentBar.getOpenClose() * signDiff)   {
				return true;
			}
		}
		return false;
	}

	public boolean isNormalDay()    {
		if(IndUtil.isValueWithinPct(currentBar.getOpenClose(), currentBar.getUpperStick(), visPct) &&
				IndUtil.isValueWithinPct(currentBar.getOpenClose(), currentBar.getLowerStick(), visPct)  )    {
			return true;
		}
		return false;
	}

	public boolean isLongDay(PriceBar comp) {
		if(isNormalDay() && comp != null)   {
			if(comp.getHighLow() * signDiff < currentBar.getHighLow())   {
				return true;
			}
		}
		return false;
	}

	public boolean isShortDay(PriceBar comp) {
		if(isNormalDay() && comp != null)   {
			if(currentBar.getHighLow() * signDiff < comp.getHighLow())   {
				return true;
			}
		}
		return false;
	}




}
