package com.atp.data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.YahooHistoricalDataReader;

public class Covariance {

	public Covariance() {

		HistoricalDataOptions options = new HistoricalDataOptions(new String[]{"GOOG", "COKE"}, 
				LocalDateTime.now().minusYears(1), LocalDateTime.now(), PriceHistory.Base.DAILY, true);
		
		PriceHistory[] qh = new YahooHistoricalDataReader().getHistoricalStockPrices(options);

		try {
			System.err.println(getPearsonCorrelation(qh[0],qh[1],0, 10));
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


	public static double getPearsonCorrelation(PriceHistory qh1,PriceHistory qh2, int offset,int window) throws Exception{
		
		if(qh1.size() != qh2.size() ) 
			throw new Exception("Data series not same length");
		
		double[] data1 = getDataSet(qh1,0, window);
		double[] data2 = getDataSet(qh2,offset, window);

		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = data1[0];
		double mean_y = data2[0];
		for(int i=2;i<data1.length+1;i+=1){
			double sweep =Double.valueOf(i-1)/i;
			double delta_x = data1[i-1]-mean_x;
			double delta_y = data2[i-1]-mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = Math.sqrt(sum_sq_x/data1.length);
		double pop_sd_y = Math.sqrt(sum_sq_y/data1.length);
		double cov_x_y = sum_coproduct / data1.length;
		result = cov_x_y / (pop_sd_x*pop_sd_y);
		return result;
	}

	public static void main(String[] args) {
		new Covariance();
	}
	
	private static double[] getDataSet(PriceHistory qh, int offset,int window) {
		int last = qh.size();
		List<PriceBar> prices = qh.getAll();
		PriceBar[] pbs = prices.toArray(new PriceBar[0]);
		List<PriceBar> data = Arrays.asList(pbs).subList(last-window-offset, last-offset);
		double[] dat = new double[data.size()];

		for(int i=0;i<data.size()-1;i++) {
			dat[i] = data.get(i).getClose();
		}
		return dat;
	}

}
