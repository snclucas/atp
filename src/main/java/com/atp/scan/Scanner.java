package com.atp.scan;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

import com.atp.data.PriceBar;
import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.Util;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.util.FormatUtil;

public class Scanner {

	private Vector<AbstractScan> scans;

	public Scanner() {}

	public void scan() {
		System.out.println("Scanning.");

		CandleStickScan gap = new CandleStickScan("Gap", CandleStickScan.GAP);
		CandleStickScan doji = new CandleStickScan("Doji", CandleStickScan.DOJI);
		CandleStickScan eng = new CandleStickScan("Engulfing", CandleStickScan.ENGULFING);
		CandleStickScan hir =new CandleStickScan("Hirami", CandleStickScan.HARAMI);
		CandleStickScan kick =new CandleStickScan("Kicker", CandleStickScan.KICKER);
		CandleStickScan longDay =new CandleStickScan("Long", CandleStickScan.LONGDAY);

		scans = new Vector<AbstractScan>();

		scans.add(new ADXScan(14));
		scans.add(new RSIScan(14, 70, 30));
		scans.add(new TAZScan(30,10));
		scans.add(gap);
		scans.add(doji);
		scans.add(eng);
		scans.add(hir);
		scans.add(kick);
		scans.add(longDay);

		//QuoteReader qr = new QuoteReader();
	//	List<String> stocks = qr.read("/Users/snclucas/Desktop/sandp500.csv");

		String[] symbols = Util.readSymbolData("C:/Users/simon/Dropbox/workspace/jta/com.atp.data/sandp100.symbols","\\t");
		
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minusMonths(36);

		HashMap<String, ScannerResults> results = new HashMap<String, ScannerResults>();

		for(String stockSymbol: symbols) {
			HistoricalDataOptions options = new HistoricalDataOptions(stockSymbol, from, to, PriceHistory.Base.DAILY, false);
			PriceHistory[] qh = new YahooHistoricalDataReader().getHistoricalStockPrices(options);

			ScannerResults scannerResult = new ScannerResults(stockSymbol);

			for(AbstractScan scan: scans) {

				Vector<ScanResult> resultsForEachScan = new Vector<ScanResult>();

				for(PriceBar priceBar:qh[0].getPriceBars()) {
					ScanResult result = scan.tick(priceBar);		
					resultsForEachScan.add(result);
				}
				scannerResult.addScanResult(scan.getName(), resultsForEachScan);
				scan.reset();
			}
			results.put(stockSymbol, scannerResult);
		}
		parseResults(results);
	}


	public void parseResults(HashMap<String, ScannerResults> results) {
		StringBuffer sb = new StringBuffer();

		String stylegreen = "style=\"background-color:green\"";
		String stylered   = "style=\"background-color:red\"";
		String stylewhite = "style=\"background-color:white\"";
		String styleblue = "style=\"background-color:blue\"";

		sb.append("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"yable2.css\" media=\"screen\" /> </head>\n");
		
		sb.append("<div id=\"tableContainer\" class=\"tableContainer\">\n ");
		sb.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"scrollTable\"> \n");
		sb.append("<thead class=\"fixedHeader\">\n ");
		
		sb.append("<tr>");
		sb.append("\t<th>Symbol</th>\n");
		
		for(AbstractScan scan: scans) {
			sb.append("\t<th>"+scan.getName()+"</th>\n");
		}
		sb.append("</tr>\n");
		
		sb.append("</thead> ");
		sb.append("<tbody class=\"scrollContent\"> ");

		for( String symbolName : results.keySet()) {
			sb.append("<tr>");
			sb.append("\t<td><a href=\"http://finance.yahoo.com/echarts?s="+symbolName+"\" target=\"_blank\" >" + symbolName + "</a></td>");

			ScannerResults resultVector = results.get(symbolName);
			
			for(String scan : resultVector.getScans()){
				Vector<ScanResult> scanResult = resultVector.getScanResult(scan);
				ScanResult lastResult = scanResult.get(scanResult.size()-1);

				String style="";

				if(lastResult.getBullOrBear() == ScanResult.BULLISH)
					style = stylegreen;
				else if(lastResult.getBullOrBear() == ScanResult.BEARISH)
					style = stylered;
				else if(lastResult.getBullOrBear() == ScanResult.SIGNAL){
					style = styleblue;	
				}
				else {
					style = stylewhite;
				}

				sb.append("<td "+style+">");
				sb.append(FormatUtil.shortNumber(lastResult.getResult()));
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</TABLE></html>");
	}


	public String colour2hex( int R, int G, int B ) {
		Color c = new Color(R,G,B);
		return Integer.toHexString( c.getRGB() & 0x00ffffff );
	}
}
