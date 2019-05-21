/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class DoubleArray2DView implements Collection<DoubleArraySlice>, RandomAccess, List<DoubleArraySlice>{
    double[] data;
    int width;
    int rows;
    int length;

    public DoubleArray2DView(double[] data, int width) {
        this.data = data;
        this.width = width;
        this.rows = data.length/width;
        this.length = rows*width;
    }
    

    public DoubleArray2DView(DoubleArrayList data, int width) {
        this.data = data.list;
        this.width = width;
        this.rows = data.size()/width;
        this.length = rows*width;
    }
    
    /** 
     * creates a copy of this view - but instaed of being an actual view - each get(row) will return a copy of the data 
     */
    public DoubleArray2DView copyOnGet() {
        return new DoubleArray2DView(data, width) {
            @Override
            public DoubleArraySlice get(int row) {
                int start = row*width;
                double[] ret = Arrays.copyOfRange(data, start, start + width);
                return new DoubleArraySlice(ret, 0, width);
            }
        };
    }
    

    @Override
    public int size() {
        return rows;
    }

    @Override
    public boolean isEmpty() {
        return rows == 0;
    }

    @Override
    public boolean contains(Object o) {
        double[] comp = null;
        if (o instanceof double[]) {
            comp = (double[]) o;
        } else if (o instanceof Double[]) {
            comp = new double[((Double[]) o).length];
            for (int r=0; r<width;r++) {
                comp[r] = ((Double[]) o)[r];
            }
        }
        
        if (comp != null) {
            outer: for (int c = 0; c<data.length;c+=width) {
                for (int r=0; r<width;r++) {
                    if (comp[r] != data[c+r]) {
                        continue outer;
                    }
                    return true;
                }
            }
            return false;
        }
        
        DoubleArraySlice das = (DoubleArraySlice) o;
        outer: for (int c = 0; c<data.length;c+=width) {
            for (int r=0; r<width;r++) {
                if (das.get(r) != data[c+r]) {
                    continue outer;
                }
                return true;
            }
        }
        return false;
        
        
    }

    
    public void quicksort(int column, boolean reverse) {
        if (reverse)
            quicksortRev(column,0,length-width);
        else
            quicksort(column,0,length-width);
    }

    public void quicksort(int column) {
        quicksort(column,0,length-width);
    }
    
    protected void quicksort(int column, int from, int to) {
        double temp;
        if (to-from > 0) {
            double pivot = data[to+column];
            int left = from;
            int right = to;
            while (left <= right) {
                while (data[left+column] < pivot) {
                    left+=width;
                }
                while (data[right+column] > pivot)
                    right-=width;
                
                if (left <= right) {
                    for (int c = 0 ; c<width;c++) {
                        temp=data[left+c];
                        data[left+c]=data[right+c];
                        data[right+c] = temp;
                    }
                    left+=width;
                    right-=width;
                }
            }
            quicksort(column,from,right);
            quicksort(column,left,to);
        }
    }


    protected void quicksortRev(int column, int from, int to) {
        double temp;
        if (to-from > 0) {
            double pivot = data[to+column];
            int left = from;
            int right = to;
            while (left <= right) {
                while (data[left+column] > pivot) {
                    left+=width;
                }
                while (data[right+column] < pivot)
                    right-=width;
                
                if (left <= right) {
                    for (int c = 0 ; c<width;c++) {
                        temp=data[left+c];
                        data[left+c]=data[right+c];
                        data[right+c] = temp;
                    }
                    left+=width;
                    right-=width;
                }
            }
            quicksortRev(column,from,right);
            quicksortRev(column,left,to);
        }
    }
    
    
    
    @Override
    public ListIterator<DoubleArraySlice> listIterator(final int initialRow) {
        return new ListIterator<DoubleArraySlice>() {
            int pos = (initialRow-1)*width;
            @Override
            public boolean hasNext() {
                return pos < length-1;
            }

            @Override
            public DoubleArraySlice next() {
                pos+=width;
                DoubleArraySlice ret = new DoubleArraySlice(data, pos, width);
                return ret;
            }

            @Override
            public boolean hasPrevious() {
                return pos>0;
            }

            @Override
            public DoubleArraySlice previous() {
                pos-=width;
                return new DoubleArraySlice(data, pos, width);
            }

            @Override
            public int nextIndex() {
                return pos/width + 1;
            }

            @Override
            public int previousIndex() {
                return pos/width - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void set(DoubleArraySlice arg0) {
                for (int i=0; i<width;i++) {
                    data[pos+i] = arg0.data[arg0.first+i];
                }
            }

            @Override
            public void add(DoubleArraySlice arg0) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    public List<DoubleArraySlice> subList(int arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<DoubleArraySlice> iterator() {
        return listIterator(0);
    }

    @Override
    public Object[] toArray() {
        DoubleArraySlice[] rowviews = new DoubleArraySlice[rows];
        for (int i = 0; i<rows;i++){
            rowviews[i] = new DoubleArraySlice(data, i*width, width);
        }
        return rowviews;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        if (arg0 instanceof DoubleArraySlice[]) {
            DoubleArraySlice[] rowviews = (DoubleArraySlice[]) arg0;
            if (rowviews.length>= rows) {
                for (int i = 0; i<rows;i++){
                    rowviews[i] = new DoubleArraySlice(data, i*width, width);
                }
            } else {
                return (T[]) toArray();
            }
        } if (arg0 instanceof double[][]) {
            double[][] ret = (double[][]) arg0;
            if (ret.length<=rows) {
                ret = new double[rows][width];
            }
            int s = 0;
            for (int row =0; row < rows; row++ ) {
                double[] t = ret[row];
                if (t== null || t.length < width) {
                    t = new double[width];
                    ret[row] = t;
                }
                for (int c = 0; c<width;c++) {
                    t[c]=data[s++];
                }
            }
            return (T[]) ret;
        }
        throw new UnsupportedOperationException("Unsuported return type"); 
        
    }

    @Override
    public boolean add(DoubleArraySlice arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        for (Object t : arg0) {
            if (!contains(t))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends DoubleArraySlice> arg0) {
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
    public boolean addAll(int arg0, Collection<? extends DoubleArraySlice> arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleArraySlice get(int row) {
        return new DoubleArraySlice(data, row*width, width);
    }

    public double get(int row, int column) {
        return data[row*width+column];
    }

    public double set(int row, int column, double value) {
        double ret =  data[row*width+column];
        data[row*width+column] = value;
        return ret;
    }

    public void setNoGet(int row, int column, double value) {
        data[row*width+column] = value;
    }
    
    @Override
    public DoubleArraySlice set(int row, DoubleArraySlice values) {
        double[] ret = new double[width];
        int start=row*width;
        for (int c = 0; c<width; c++) {
            ret[c]=data[start];
            data[start++] = values.get(c);
        }
        return new DoubleArraySlice(ret, 0, width);
    }

    @Override
    public void add(int arg0, DoubleArraySlice arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleArraySlice remove(int arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int indexOf(Object row) {
        if (row instanceof DoubleArraySlice || row instanceof double[]) {
            for (int r = 0; r<rows;r++)
                if (new DoubleArraySlice(data, r*width, width).equals(row)) {
                    return r;
                }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object row) {
        if (row instanceof DoubleArraySlice || row instanceof double[]) {
            for (int r = rows-1; r>=0;r--)
                if (new DoubleArraySlice(data, r*width, width).equals(row)) {
                    return r;
                }
        }
        return -1;
    }

    @Override
    public ListIterator<DoubleArraySlice> listIterator() {
        return listIterator(0);
    }
    
    
}
