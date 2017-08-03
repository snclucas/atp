package com.atp.maths;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atp.data.Covariance;
import com.atp.data.io.PriceHistory;
import com.atp.util.FormatUtil;
import com.atp.util.PropertiesUtil;

public class CorrelationMatrix {

	private List<PriceHistory> histories;
	private int window;
	private int offset;
	private int[][] calculated;
	private double[][] cavariances;

	public CorrelationMatrix(int window, int offset) {
		this.window = window;
		this.offset = offset;
		histories = new ArrayList<PriceHistory>();
	}



	public double[][] createMatrix() {
		int numHistories = histories.size();
		calculated = new int[numHistories][numHistories];
		cavariances = new double[numHistories][numHistories];
		

		int i = 0; 
		for(PriceHistory qh : histories) {
			int j = 0;
			for(PriceHistory qh2 : histories) {
				cavariances[i][j] = -1;
				
				if(calculated[i][j]==0) {

					try {
						double cov = Covariance.getPearsonCorrelation(qh, qh2, offset, window);
						cavariances[i][j] = (cov*1000);
						//if(cavariances[i][j]<0) cavariances[i][j] = -1;
						System.out.println(i+" "+qh.getSymbol() + " " + j+" "+qh2.getSymbol() + " " + FormatUtil.small(cov));

						//no need to duplicate
						calculated[i][j] = calculated[j][i] = 1;
					}
					catch(Exception e) {
						e.printStackTrace();
					}

				}

				j++;
			}
			i++;
		}

		return cavariances;
	}


	public void printHTML() {
		int numHistories = histories.size();
		StringBuffer sb = new StringBuffer();

		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"yable2.css\" media=\"screen\" />");
		sb.append("</head>\n");
		sb.append("<body>\n");

		sb.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> \n");


		sb.append("<tr>");
		sb.append("\t<th> - </th>\n");

		for(PriceHistory h: histories) {
			sb.append("\t<th>"+h.getSymbol()+"</th>\n");
		}
		sb.append("</tr>\n");

		

		for(int i = 0;i<numHistories;i++) {
			//row
			sb.append("<tr>");
			sb.append("<td>" + histories.get(i).getSymbol() + "</td>");
			
			
			for(int j = 0;j<numHistories;j++) {
				
				                              
				String style = "";
				
				if(cavariances[i][j] != -1) {
					int value = (int) (255*((cavariances[i][j]-998)/2));
					if(value<=0) value = 0;

					style = "style=\"background-color:"+colour2hex( 255, 255-value,255-value) +"\"";
					sb.append("<td "+style+">" + FormatUtil.small(cavariances[i][j]) + "</td>");
				}
				else 
					sb.append("<td> </td>");
					
			}
			
			
			sb.append("</tr>");
		}


		sb.append("</TABLE>");

		sb.append("</body>\n");
		sb.append("</html>");




		try {
			String filename = PropertiesUtil.pathToResults + "corr.html";
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			String outText = sb.toString();
			out.write(outText);
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}


	}

	public void printMatrix() {
		int numHistories = histories.size();

		for(int i = 0;i<numHistories-1;i++)
			for(int j = 0;j<numHistories-1;j++)
				System.out.println(i + " " + j + " " + FormatUtil.small(cavariances[i][j]));


	}



	public void addPriceHistory(PriceHistory ... qh) {
		histories.addAll(Arrays.asList(qh));
	}


	public void addPriceHistory(PriceHistory qh) {
		histories.add(qh);
	}



	public List<PriceHistory> getHistories() {
		return histories;
	}



	public void setHistories(List<PriceHistory> histories) {
		this.histories = histories;
	}



	public int getWindow() {
		return window;
	}



	public void setWindow(int window) {
		this.window = window;
	}



	public int getOffset() {
		return offset;
	}



	public void setOffset(int offset) {
		this.offset = offset;
	}



	public int[][] getCalculated() {
		return calculated;
	}



	public void setCalculated(int[][] calculated) {
		this.calculated = calculated;
	}



	public double[][] getCavariances() {
		return cavariances;
	}



	public void setCavariances(double[][] cavariances) {
		this.cavariances = cavariances;
	}


	public String colour2hex( int R, int G, int B ) {
		Color c = new Color(R,G,B);
		return Integer.toHexString( c.getRGB() & 0x00ffffff );
	}


}
