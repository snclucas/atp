package com.atp.scan;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;
import com.atp.indicators.IndUtil;
import com.atp.maths.MathUtil;
import com.atp.maths.Stats;
import com.atp.util.DateUtil;

public class CandleStickScan extends AbstractScan{

	private static  final   float   ignPct      = 5;    // 5%  is ignoring value percentage
	//private static  final   float   visPct      = 20;   // 20% is visible value percentage
	//private static  final   int     allPct      = 90;   // 90% is about equal to all of it
	private static  final   int     hugeDiff    = 10;   // 10 times is huge difference
	//private static  final   int     largeDiff   = 5;    // 5  times is large difference
	//private static  final   int     signDiff    = 2;    // 2  times is significant difference


	public final static int DOJI = 0;
	public final static int ENGULFING = 1;
	public final static int KICKER = 2;
	public final static int HARAMI = 3;
	public final static int GAP = 4;
	public final static int LONGDAY = 5;


	private PriceBar today;
	private PriceBar yesterday;
	private PriceBar dayBeforeYesterday;
	private int type;

	private double closeToday;
	private double closeYesterday;
	private double closeDayBeforeYesterday;
	private double openToday;
	private double openYesterday;
	private double openDayBeforeYesterday;
	private double highLowToday;
	private double highLowYesterday;
	private double highLowDayBeforeYesterday;
	private double openCloseToday;
	private double openCloseYesterday;
	private double openCloseDayBeforeYesterday;
	private boolean todayIsWhite;
	private boolean todayIsBlack;
	private boolean yesterdayIsWhite;
	private boolean yesterdayIsBlack;


	public CandleStickScan(int type) {
		this("Candle",type);
	}
	
	public CandleStickScan(int type, boolean justLastDay) {
		this("Candle",type, justLastDay);
	}

	public CandleStickScan(String name, int type) {
		super(name, true);
		this.type = type;
		priceBars = new ArrayList<PriceBar>();
	}
	
	public CandleStickScan(String name, int type, boolean justLastDay) {
		super(name, justLastDay);
		this.type = type;
		priceBars = new ArrayList<PriceBar>();
	}



	public ScanResult tick(PriceBar priceBar) {
		priceBars.add(priceBar);

		if(priceBars.size()<3) return new ScanResult(null,name,name,0,-99, 0);

		LocalDateTime compare = LocalDateTime.now().minusDays(1);
		
		while(!DateUtil.isTradingDay(compare))
			compare = compare.minusDays(1);
			

		today = priceBar;
		yesterday = priceBars.get(priceBars.size()-2);
		dayBeforeYesterday = priceBars.get(priceBars.size()-3);

		closeToday = today.getClose();
		closeYesterday = yesterday.getClose();
		closeDayBeforeYesterday = dayBeforeYesterday.getClose();

		openToday = today.getOpen();
		openYesterday = yesterday.getOpen();
		openDayBeforeYesterday = dayBeforeYesterday.getOpen();

		highLowToday = today.getHighLow();
		highLowYesterday = yesterday.getHighLow();
		highLowDayBeforeYesterday = dayBeforeYesterday.getHighLow();

		openCloseToday = today.getOpenClose();
		openCloseYesterday = yesterday.getOpenClose();
		openCloseDayBeforeYesterday = dayBeforeYesterday.getOpenClose();

		todayIsWhite = today.isWhiteCandle();
		todayIsBlack = today.isBlackCandle();

		yesterdayIsWhite = yesterday.isWhiteCandle();
		yesterdayIsBlack = yesterday.isBlackCandle();
		
		int signal = 0; 
		ScanResult result = new ScanResult(null,name,name,0,-990, 0);
		
		if(compare.toLocalDate().compareTo(priceBar.getDateTime().toLocalDate()) == 0 || !justLastDay) {
			
			if(type == DOJI) {
				signal = someKindOfDoji(openCloseToday);
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);
			}
			else if(type == ENGULFING) {
				signal = engulfing(0.0);
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);	
			}
			else if(type == KICKER) {
				signal = kicker();
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);
			}
			else if(type == HARAMI) {
				signal = harami();
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);
			}
			else if(type == GAP) {
				signal = gap(0.5);
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);
			}
			else if(type == LONGDAY) {
				signal = longDay(30,4);
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, signal, 1);
			}
			
		}
		
		return result;
	}


	public int gap() {	
		return gap(0);
	}
	
	
	public int kicker() {
		
		if(isSomeKindOfDoji(openCloseYesterday) || isSomeKindOfDoji(openCloseToday))
			return ScanResult.NOTHING;
		
		if(yesterdayIsWhite && todayIsBlack  && MathUtil.withinPc(openYesterday, openToday, 0.0 )     ) {
			return ScanResult.BEARISH;
		}
		else if(yesterdayIsBlack && todayIsWhite  && MathUtil.withinPc(openYesterday, openToday, 0.0 )     ) {
			return ScanResult.BULLISH;
		}
		else 
			return ScanResult.NOTHING;
		
	}

	public int gap(double percentFactor) {	
		double pcf = 1+(percentFactor/100.0);
		if(Math.min(closeToday, openToday)  >  (Math.max(closeYesterday, openYesterday)+openCloseYesterday*pcf) )
			return ScanResult.BULLISH;
		else if(Math.max(closeToday, openToday)  <  (Math.min(closeYesterday, openYesterday)-openCloseYesterday*pcf) )
			return ScanResult.BEARISH;
		else
			return ScanResult.NOTHING;
	}



	public int engulfing() {
		return engulfing(0);
	}
	
	public int engulfing(double percentFactor) {
		double pcf = 1+(percentFactor/100.0);
		if(yesterdayIsWhite && (openToday>closeYesterday*pcf)  && (closeToday*pcf < openYesterday)  )
			return ScanResult.BEARISH;
		else if (yesterdayIsBlack && (openToday*pcf<closeYesterday)  && (closeToday > openYesterday*pcf)  )
			return ScanResult.BULLISH;
		else 
			return ScanResult.NOTHING;
	}



	/* Criteria

   1. The body of the first candle is black, the body of the second candle is white.

   2. The downtrend has been evident for a good period. A long black candle occurs at the end of the trend.

   3. The second day opens higher than the close of the previous day and 
   closes lower than the open of the prior day.

   4. Unlike the Western �Inside Day�, just the body needs to remain in the previous day's body, 
   where as the �Inside Day� requires both the body and the shadows to remain inside the previous day's body.

   5. For a reversal signal, further confirmation is required to indicate that the trend is now moving up. 

Signal Enhancements

   1. The longer the black candle and the white candle, the more forceful the reversal.
   2. The higher the white candle closes up on the black candle, the more convincing that
    a reversal has occurred despite the size of the white candle */


	public int harami() {
		if( todayIsBlack && yesterdayIsWhite && (closeToday > openYesterday)  && openToday < closeYesterday ) {
			return ScanResult.BEARISH;
		}
		if( todayIsWhite && yesterdayIsBlack && (closeToday < openYesterday)  && openToday > closeYesterday ) {
			return ScanResult.BULLISH;
		}
		return ScanResult.NOTHING;
	}

	
	
	public int longDay(int period, int mult) {	
		double sd = Stats.sdReturns(priceBars, period, QuoteHistory.OPENCLOSE);
		if(today.getOpenClose() > sd*mult) {
			return ScanResult.SIGNAL;
		}
		return ScanResult.NOTHING;
	}


	public boolean isSomeKindOfDoji(double openClose) {
		if(openClose == 0)
			return true;
		return false;
	}

	public int someKindOfDoji(double openClose) {
		if(openClose == 0)
			return ScanResult.SIGNAL;
		return ScanResult.NOTHING;
	}

	public int someDoji() {
		if(openCloseToday * hugeDiff < highLowToday && IndUtil.isValueWithinPct(openToday, closeToday, ignPct))      {
			return ScanResult.SIGNAL;
		}
		return ScanResult.NOTHING;
	}
	



}
