package com.atp.maths;

public class MathUtil {
	
	
	
	public static boolean withinPc(double val1, double val2, double withinPc) {
		withinPc = (withinPc/100.0);
		return Math.abs(((val1-val2 )/val1)) <= withinPc;
	}


}
