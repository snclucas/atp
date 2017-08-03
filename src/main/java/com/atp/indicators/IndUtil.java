package com.atp.indicators;

import java.util.Arrays;

/**
 * This class implements basic com.atp.indicators calculated over series of com.atp.data
 *
 */
public class IndUtil {
    
    public  static  final   String  insRngErrMsg = "Insufficient Range";
    
    /** Creates a new instance of IndUtil */
    public IndUtil() {
    }
    
    /**
     * Calculate Simple Moving Average (SMA)
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return          SMA value
     */
    public  static  float getSma(float[] range, int barIdx, int length)  {
        double rval = 0;
        
        if (length <= 0 || barIdx < 0 || barIdx + length > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        for(int i = 0; i < length; i++)  {
            rval += range[barIdx + i];
        }
        
        return (float)(rval / length);
    }
    
    /**
     * Gets SMA series (for performance)
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @param seqLength series length
     * @return          SMA series
     */
    public  static  float[] getSma(float[] range, int barIdx, int length, int seqLength)    {
        
        if (length <= 0 || barIdx < 0 || seqLength <= 0 || barIdx + seqLength + length > range.length)  {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        double ssma = 0; // starting SMA
        
        for(int i = 0; i < length - 1; i++) {
            ssma += range[barIdx + seqLength + i];
        }
        
        float[] rval = new float[seqLength];
        
        for(int i = seqLength - 1; i >= 0; i--) {
            ssma += range[barIdx + i];
            rval[i] = (float)(ssma / length);

            ssma -= range[barIdx + i + length]; // substract the oldest price
        }
        
        return rval;
    }
    
    /**
     * Calculate Simple Moving Average (SMA)
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return          SMA value
     */
    public  static  long    getSma(long[] range, int barIdx, int length)    {
        long rval = 0;
        
        if (length <= 0 || barIdx < 0 || barIdx + length > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        for(int i = 0; i < length; i++)  {
            rval += range[barIdx + i];
        }
        
        return rval / length;
    }
    
    /**
     * Calculate Exponential Moving Average (EMA)
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return          SMA value
     */
    public  static  float getEma(float[] range, int barIdx, int length)  {
        double rval = 0;
        
        if (length <= 0 || barIdx < 0 || barIdx + length * 2 > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        double mul = 2 / (1 + (double)length);
        rval = getSma(range, barIdx + length, length);
        
        for(int i = length - 1; i >= 0; i--)    {            
            rval = (range[barIdx + i] - rval) * mul + rval;
        }
        
        return (float)rval;
    }
    
    public  static  float[] getEma(float[] range, int barIdx, int length, int seqLength)    {
        
        if (length <= 0 || barIdx < 0 || seqLength <= 0 || barIdx + seqLength + length * 2 > range.length)  {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        double mul = 2 / (1 + (double)length);
        float[] rval = new float[seqLength];

        double ema = getEma(range, barIdx + seqLength, length);
        
        for(int i = seqLength - 1; i >= 0; i--) {
            ema = (range[barIdx + i] - ema) * mul + ema;
            rval[i] = (float)ema;
        }
        
        return rval;
    }

    /**
     * Calculate Moving Median
     * Moving median arguably is more precise in identifing better support/resistance then MAs
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return          MM value
     */
    public  static  float   getMm(float[] range, int barIdx, int length)    {
        float rval = 0;
        
        if (length <= 0 || barIdx < 0 || barIdx + length > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        float[] series = new float[length];
        for(int i = 0; i < length; i++)  {
            series[i] = range[barIdx + i];
        }
        Arrays.sort(series, 0, length - 1);
        
        if(length % 2 == 0) {
            rval = (series[length / 2] + series[length / 2 + 1]) / 2;
        }
        else    {
            rval = series[length / 2];
        }
        
        return rval;
    }
    
    /**
     * Gets Moving Median series (for performance)
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @param seqLength series length
     * @return          MM series
     */
    public  static  float[] getMm(float[] range, int barIdx, int length, int seqLength)    {
        
        if (length <= 0 || barIdx < 0 || seqLength <= 0 || barIdx + seqLength + length > range.length)  {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        float[] series = new float[length];
        float[] rval   = new float[seqLength];
        
        for(int i = seqLength - 1; i >= 0; i--) {
            
            for(int j = 0; j < length - 1; j++) {
                series[j] = range[barIdx + i + 1 + j];
            }

            Arrays.sort(series, 0, length - 1);
        
            float mval;
            
            if(length % 2 == 0) {
                mval = (series[length / 2] + series[length / 2 + 1]) / 2;
            }
            else    {
                mval = series[length / 2];
            }
            
            rval[i] = mval;
        }
        return rval;
    }

    /**
     * Get value based on percentage
     *
     * @param baseVal base value
     * @param pct percentage
     * @return  percentage of value
     */
    public  static  double  getValueOfPct(double baseVal, float pct)    {
        return (baseVal * pct) / 100;
    }
    
    /**
     * Get percentage based on value
     *
     * @param baseVal base value
     * @param val   aux value
     * @return  percentage
     */
    public  static  float   getPctOfValue(double baseVal, double val)   {
        return (float)((val * 100) / baseVal);
    }
    
    /**
     * Checks if value is within specified percentage
     *
     * @param baseVal base value
     * @param val     tested vaue
     * @param pct     percentage
     */
    public  static  boolean isValueWithinPct(double baseVal, double val, float pct) {
        boolean rval = false;
        
        double diff = Math.abs(baseVal - val);
        if(getPctOfValue(baseVal, diff) <= pct) {
            rval = true;
        }
        return rval;
    }
    
    /**
     * Gets standard deviation across the range of values
     *
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return returns standard deviation
     */
    public  static  float   getStdDev(float[] range, int barIdx, int length)    {
         
        if (length <= 0 || barIdx < 0 || barIdx + length > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
       
        double sum   = 0;
        double sumSq = 0;
        
        for(int i = 0; i < length; i++)  {
            float val = range[barIdx + i];
            sum += val;
            sumSq += Math.pow(val, 2);
        }
        double stddev = Math.sqrt(sumSq - (sum * (sum / length)) / (length - 1));
        return (float)stddev;
    }

    /**
     * 
     * @param range     com.atp.data series
     * @param barIdx    index to calculate com.atp.data for
     * @param length    length
     * @return linear regression
     */
    public  static  float[] getLinReg(float[] range, int barIdx, int length)    {
         
        if (length <= 0 || barIdx < 0 || barIdx + length > range.length)    {
            throw new IllegalArgumentException(insRngErrMsg);
        }
        
        float[] linreg = new float[length];

        double sum_x   = 0;
        double sum_y   = 0;
        double sum_xy  = 0;
        double sum_xSq = 0;
        
        for(int i = 0; i < length; i++)  {
            float val = range[barIdx + i];
            sum_x   += i;
            sum_xSq += Math.pow(i, 2);
            sum_y   += val;
            sum_xy  += i * val;
        }
        
        double m = (length * sum_xy - sum_x * sum_y) / (length * sum_xSq - Math.pow(sum_x, 2));
        double b = (sum_y - m * sum_x) / length;
        
        for(int i = 0; i < length; i++) {
            linreg[i] = (float)(m * i + b);
        }
        
        return linreg;
    }
    
}
