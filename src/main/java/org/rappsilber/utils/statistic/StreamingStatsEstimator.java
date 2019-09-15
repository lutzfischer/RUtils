/* 
 * Copyright 2016 Lutz Fischer <l.fischer@ed.ac.uk>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rappsilber.utils.statistic;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rappsilber.utils.UpdateableInteger;

/**
 * estimates a Median by splitting the assumed observed range into windows and count the occurrences within these windows.
 * Basically making a histogram and then calculating the histogramMedian for this histogram.
 * Which is hopefully a close enough approximation of the real median.
 * @author Lutz Fischer <l.fischer@ed.ac.uk>
 */
public class StreamingStatsEstimator {
    public class BinValue {
        int count;
        double sum;
        double min;
        double max;

        public BinValue() {
        }

        public BinValue(double value) {
            this.count = 1;
            this.sum = value;
            this.min = value;
            this.max = value;
        }

        public BinValue(BinValue v) {
            this.count = v.count;
            this.sum = v.sum;
            this.min = v.min;
            this.max = v.max;
        }
        
        public double average() {
            return sum/count;
        }
        
        
        public void add(double value) {
            count++;
            sum+=value;
            if (value < min) {
                min = value;
            } else if (value > max)
                max = value;
        }
        public void add(BinValue v) {
            count+=v.count;
            sum+=v.sum;
            if (v.min < min)
                min = v.min;
            if (v.max > max)
                max = v.max;
        }
    }
    protected TreeMap<Double, BinValue> m_values = new TreeMap<Double, BinValue>();
    protected double m_resolution = 0.001;
    protected int    m_maxWindows = 1000;
    
    private int m_count;
    private double m_avg;
    private double m_min;
    private double m_max;
    private double m_mean2;

    public StreamingStatsEstimator(double resolution) {
        this.m_resolution = resolution;
    }

    public StreamingStatsEstimator() {
    }
    
    public StreamingStatsEstimator(double resolution, int maxBins) {
        this(resolution);
        m_maxWindows = maxBins;
    }


    protected void reduceResolution() {
        // make sure nobody else is currently doing anything to the values
        // semcount - 1 as we already acquired one permit
        
        
        // now we should be the only ones doing anything to the values
        int ws = m_values.size();
        // we should only get in here if we exceded the number of possible windows
        // but as we are in a multithreaded enviroment we could have ended up in here 
        // several times in parrallel
        // so a previous call might have already taken care of it.
        //if (ws >= m_maxWindows) { 
            // we would exceed the maximum number of keys
            // so we are the first to tread the current limit
            double newResolution = m_resolution * 2;
            TreeMap<Double, BinValue> old_values = m_values;
            TreeMap<Double, BinValue> new_values = new TreeMap<Double, BinValue>();

            // go through all bins of old values and transfer the data to the new bins
            for (Double oldbin : old_values.keySet()) {
                Double newbin = Math.round(oldbin/newResolution)*newResolution;
                BinValue newBinValue = new_values.get(newbin);
                BinValue oldBinValue = old_values.get(oldbin);
                if (newBinValue != null) {
                    newBinValue.add(oldBinValue);
                } else {
                    newBinValue = new BinValue(oldBinValue);
                    new_values.put(newbin, newBinValue);
                }
            }
            m_values = new_values;
            m_resolution = newResolution;
        //}
    }

    public void addValue(double d) {
        Double key = Math.round(d/m_resolution)*m_resolution;
        BinValue i = m_values.get(key);

        m_count++;
        double delta = d - m_avg;
        m_avg += delta / m_count;
        m_mean2 += delta * (d - m_avg);
        if (m_min > d)
            m_min = d;
        else if (m_max < d)
            m_max = d;
        
        
        if (i == null) {

            int ws = m_values.size();

            if (ws >= m_maxWindows) { 
                // we would exceed the maximum number of keys
                // so we have to reduce the resolution
                reduceResolution();
            } else {
                i= new BinValue(d);
                m_values.put(key, i);
            }
        } else {
            i.add(d);
        }
    }

    public double getMedianEstimation() {
        // count how many we have
        TreeMap<Double, BinValue> v = m_values;
        return histogramMedian(v, m_count);
    }

    protected double histogram_monotonic_rate(int stretch, SortedMap<Double,BinValue> values) {
        return histogram_monotonic_rate(stretch, values, m_min, m_max);
    }

    protected double histogram_monotonic_rate(int stretch, SortedMap<Double,BinValue> values, double from, double to) {
        int currentstretch = 0;
        int currentstretch_sum = 0;
        int total_stretch = 0;
        int total_sum = 0;
        boolean up = true;
        int last =0;
        for (Map.Entry<Double,BinValue> b : values.subMap(from, to).entrySet()) {
            int count = b.getValue().count;
            total_sum += count;
            if (up)  {
                if (count >= last) {
                    currentstretch ++;
                    currentstretch_sum += count;
                } else {
                    up = false;
                    if (currentstretch >= stretch) {
                        total_stretch += currentstretch_sum;
                    }
                    currentstretch_sum = count;
                    currentstretch = 1;
                }
            } else {
                if (count <= last) {
                    currentstretch ++;
                    currentstretch_sum += count;
                }else {
                    up = true;
                    if (currentstretch >= stretch) {
                        total_stretch += currentstretch_sum;
                    }
                    currentstretch_sum = count;
                    currentstretch = 1;
                }
            }
            last = count;
        }
        return total_stretch/(double) total_sum;
    }
    
    public StreamingStatsEstimator clone() {
        StreamingStatsEstimator n = new StreamingStatsEstimator();
        n.m_avg = m_avg;
        n.m_count = m_count;
        n.m_max = m_max;
        n.m_mean2 = m_mean2;
        n.m_maxWindows = m_maxWindows;
        n.m_min = m_min;
        n.m_resolution = m_resolution;
        n.m_values = (TreeMap<Double, BinValue>) m_values.clone();
        return n;
    }
    
    public StreamingStatsEstimator getMostlyMonotonic() {
        return getMostlyMonotonic(m_min, m_max);
    }
    
    public StreamingStatsEstimator getMostlyMonotonic(double from, double to) {
        int stretch = 10;
        SortedMap<Double,BinValue> ftvalues = m_values.subMap(from, to);
        if (ftvalues.size() <50) {
            stretch = ftvalues.size()/5;
            if (stretch <2) {
                stretch = 3;
            }
            if(ftvalues.size()< stretch*3)
                return this;
        }
        if (histogram_monotonic_rate(stretch, ftvalues)>0.5) {
            return this;
        }
        StreamingStatsEstimator ret = this.clone();
        ret.reduceResolution();
        while (ret.m_values.size()> stretch*3 && histogram_monotonic_rate(stretch, ret.m_values)<=0.5) {
            ret.reduceResolution();
        }
        return ret;
    }
    
    public double getModeEstimation() {
        // count how many we have
        double mode = Double.NaN;
        // if the data look to noisy we will just reduce the resolution a bit.
        StreamingStatsEstimator sse =  getMostlyMonotonic();
        TreeMap<Double, BinValue> v = sse.m_values;
        mode = histogramMode(v);
        
        return mode;
    }
    
    public static double histogramMedian(TreeMap<Double, BinValue> v, int allcounts) {
        double dCenter = allcounts /2.0;
        int iCenter = (int)Math.round(dCenter);
        int iCenter1 = (int)iCenter;
        
        if (iCenter == dCenter)
            iCenter1++;
            
        int count=0;
        BinValue last =v.firstEntry().getValue();
        // find the middle bin
        for (Map.Entry<Double,BinValue> b : v.entrySet()) {
            BinValue bv = b.getValue();
            // we have the exact median
            if (count==iCenter) {
                if (iCenter != iCenter1){
                    return (last.max + bv.min)/2;
                } else
                    return last.max;
            }
            int countlast=count+b.getValue().count;
            
            // the median is somewher in this bin
            if (countlast > iCenter) {
                // assume the getAverage of this bin is the value int the middle 
                // of the binned values.
                int bvmiddle = (int)b.getValue().count/2;
                
                // what is the middle of the bin in terms of the whole dataset
                int countmiddle = count+bvmiddle;
                
                double average = bv.average();
                
                // now we can estimate the median based on that assumption
                if (countmiddle > iCenter) {
                    int offcenter = countmiddle-iCenter;
                    int halfBinCount = (bv.count/2);
                    
                    if (iCenter == iCenter1)
                        return average - ((average-bv.min)/halfBinCount*offcenter);
                    else 
                        return (average - ((average-bv.min)/halfBinCount*offcenter) +
                                average - ((average-bv.min)/halfBinCount*(offcenter+1)))/2;
                } else {
                    int offcenter = iCenter - countmiddle;
                    int halfBinCount = (bv.count/2);
                    
                    if (iCenter == iCenter1)
                        return average + ((bv.max - average)/halfBinCount*offcenter);
                    else 
                        return (average + ((bv.max - average)/halfBinCount*offcenter) +
                                average + ((bv.max - average)/halfBinCount*(offcenter+1)))/2;
                }            
            }
            count=countlast;

            last = b.getValue();
        }
        throw new IndexOutOfBoundsException("Median should not be outside the list of values");
    }

    public static double histogramMode(Map<Double, BinValue> v) {
        long max =0;
        double mode = Double.NaN;
        BinValue first = v.values().iterator().next();
        BinValue bv0 = first;
        BinValue bv1 = first;
        BinValue bv2 = first;
        BinValue bv3 = first;
        BinValue bv4 = first;
        BinValue modebv = first;
        for (BinValue bv : v.values()) {
            long c = bv0.count+bv1.count*2+bv2.count*3+bv3.count*2+bv4.count;
            if (c > max) {
                max=c;
                mode=bv2.average();
                modebv = bv2;
            }
            bv0 = bv1;
            bv1 = bv2;
            bv2 = bv3;
            bv3 = bv4;
            bv4 = bv;
        }
        long c = bv2.count+bv3.count*2+bv4.count*6;
        if (c > max) {
            max=c;
            mode=bv4.average();
        }
        
        
        return mode;
    }

    public double getModeMADEstimation() {
        return getMADEstimation(histogramMode(m_values));
    }
    
    public double getMADEstimation() {
        return getMADEstimation(histogramMedian(m_values,m_count));
    }
    
    public double getMADEstimation(double center) {
        TreeMap<Double,BinValue> deviations = new TreeMap<>();
        double median = center;
        
        // create a new histogram of ditances
        for (BinValue bv : m_values.values()) {
            // for each bin create a new one that represents the distance
            BinValue distBV = new BinValue(bv);
            distBV.min = Math.abs(distBV.min-median);
            distBV.max = Math.abs(distBV.max-median);
            distBV.sum = Math.abs((distBV.average()-median)*distBV.count);
            BinValue ebv = deviations.get(distBV.average());
            if (ebv == null) {
                deviations.put(distBV.average(), distBV);
            } else {
                ebv.add(distBV);
            }
        }
        
        return histogramMedian(deviations, m_count);
    }

    /**
     * returns the getAverage over all seen values
     * @return
     */
    public double getAverage() {
        return m_avg;
    }

    /**
     * returns the standard deviation over all seen values
     * @return
     */
    public double getStdDev() {
        return Math.sqrt(m_mean2 / m_count);
    }

    public double getMin() {
        return m_min;
    }

    public double getMax() {
        return m_max;
    }
    
    public static void main(String[] args) {
        StreamingStatsEstimator e = new StreamingStatsEstimator(0.0001, 1000);
        Random r = new Random(1234);
        for (int i  =1; i< 10000000; i++) {
            e.addValue(3+r.nextGaussian());
            if (i%10 == 0)
                e.addValue(10+r.nextGaussian());
        }
        
        System.out.println("avergae : " +e.getAverage() +"\nmedia:" + e.getMedianEstimation());
        System.out.println("mad:" + e.getMADEstimation());
        System.out.println("StDev:" + e.getStdDev());
        System.out.println("StDev(MAD):" + e.getMADEstimation()*1.4826);
        System.out.println("mode:" + e.getModeEstimation());
        System.out.println("MAD on mode:" + e.getMADEstimation(e.getModeEstimation()));
        System.out.println("StDev(MAD on mode):" + e.getMADEstimation(e.getModeEstimation())*1.4826);
        System.out.println("min:" + e.m_min);
        System.out.println("max:" + e.m_max);
        
    }
    
    public String dumpCSV() {
        StringBuilder sb = new StringBuilder("bin, average, count, min, max, sum\n");
        for (Map.Entry<Double,BinValue> e : m_values.entrySet() ) {
            BinValue bv = e.getValue();
            sb.append(e.getKey()).append(", ");
            sb.append(bv.average()).append(", ");
            sb.append(bv.count).append(", ");
            sb.append(bv.min).append(", ");
            sb.append(bv.max).append(", ");
            sb.append(bv.sum).append("\n");
        }
        return sb.toString();
    }
}
