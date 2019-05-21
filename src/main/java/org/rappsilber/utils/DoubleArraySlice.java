/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class DoubleArraySlice implements List<Double>, RandomAccess {
    double[] data;
    int first;
    int length;
    int last;

    public DoubleArraySlice(double[] data, int first, int length) {
        this.data = data;
        this.first = first;
        this.length = length;
        this.last = first+length-1;
    }

    
    
    
    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean contains(Object arg0) {
        Double d = (Double) arg0;
        for (int i = first ; i<=last; i++)
            if (d == data[i])
                return true;
        return false;
    }

    @Override
    public Iterator<Double> iterator() {
        return this.listIterator(0);
    }

    @Override
    public Object[] toArray() {
        Double[] ret = new Double[length];
        for (int i = 0; i < length; i++) 
            ret[i] = data[first+i];
        return ret;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        if (arg0 instanceof Double[]) {
            Double[] ret = (Double[]) arg0;
            if (ret.length<length)
                ret = new Double[length];
            
            for (int i = 0; i < length; i++) 
                ret[i] = data[first+i];
            return (T[]) ret;
        } else {
            throw new UnsupportedOperationException("Conversion to non double not suported"); 
        }
    }

    @Override
    public boolean add(Double arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        for (Iterator<?> it = arg0.iterator(); it.hasNext();) {
            if (!contains(it.next()))
                return false;
            
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Double> arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int arg0, Collection<? extends Double> arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double get(int pos) {
        return data[first+pos];
    }

    @Override
    public Double set(int pos, Double value) {
        int p  = first+pos;
        double ret = data[p] ;
        data[p] = value;
        return ret;
    }

    @Override
    public void add(int arg0, Double arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double remove(int arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int indexOf(Object value) {
        for (int p = first; p<=last; p++) {
            if (data[p]==(Double)value)
                return p-first;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object value) {
        for (int p = last; p>=first; p--) {
            if (data[p]==(Double)value)
                return p-first;
        }
        return -1;
    }

    @Override
    public ListIterator<Double> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<Double> listIterator(int nextPos) {
        return new ListIterator<Double>() {
            int cp = first+nextPos-1;
            
            @Override
            public boolean hasNext() {
                return cp<last;
            }

            @Override
            public Double next() {
               return data[++cp];
            }

            @Override
            public boolean hasPrevious() {
                return cp>first;
            }

            @Override
            public Double previous() {
                return data[--cp];
            }

            @Override
            public int nextIndex() {
                return cp-first+1;
            }

            @Override
            public int previousIndex() {
                return cp-first-1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void set(Double value) {
                data[cp] = value;
            }

            @Override
            public void add(Double arg0) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
                
    }

    @Override
    public List<Double> subList(int from, int to) {
        return new DoubleArraySlice(data,from,to-from);
    }
    
    
    public boolean equals(DoubleArraySlice o) {
        if (this.length != o.length)
            return false;
        for (int p = 0; p<o.length;p++) {
            if (o.get(p) != get(p))
                return false;
        }
        return true;
    }

    public boolean equals(double[] o) {
        if (this.length != o.length)
            return false;
        for (int p = 0; p<o.length;p++) {
            if (o[p] != get(p))
                return false;
        }
        return true;
    }
    
}
