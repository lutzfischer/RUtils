/*
 * Copyright 2015 Lutz Fischer <lfischer at staffmail.ed.ac.uk>.
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
package org.rappsilber.utils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * Stores a two dimensional array with a one variable dimension.
 * Currently this can only grow (add only)
 * @author lfischer
 */
public class DoubleArrayList2D implements Collection<List<Double>>, List<List<Double>>, RandomAccess {
    double[] list;
    int width;
    int count=0;
    int rows=0;

    
    public class RowView implements List<Double> {
        int start;
        int end;

        protected RowView(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public RowView(int row) {
            this(row*width,row*width+width-1);
        }
        

        @Override
        public int size() {
            return width;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object arg0) {
            double d = (double) arg0;
            for (int i = start; i<=end; i++) {
                if (d == list[i])
                    return true;
            }
            return false;
        }

        @Override
        public Iterator<Double> iterator() {
            return new Iterator<Double>() {
                int pos = start-1;
                @Override
                public boolean hasNext() {
                    return pos<end;
                }

                @Override
                public Double next() {
                    return list[++pos];
                }
            };
        }

        @Override
        public Object[] toArray() {
            Double[] ret = new Double[width];
            int d=0;
            for (int i = start; i<= end; i++) 
                ret[d++] = list[i];
            return ret;
        }

        @Override
        public <T> T[] toArray(T[] arg0) {
            if (arg0 instanceof Double[]) {
                Double[] ret;
                if (((Double[]) arg0).length < width) {
                    ret =  new Double[width];
                } else 
                    ret = (Double[]) arg0;
                int d=0;
                for (int i = start; i<= end; i++) 
                    ret[d++] = list[i];
                return (T[]) ret;
            }
            throw new InvalidParameterException("Only Double arrays supported");
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
            
            for (Object d : arg0) {
                if (!contains(d))
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
            return list[start+pos];
        }

        @Override
        public Double set(int pos, Double value) {
            Double r = list[start+pos];
            list[start+pos] = value;
            return r;
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
        public int indexOf(Object arg0) {
            for (int i=0; i<width;i++) {
                if (list[start+i] == (Double)arg0)
                    return i;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object arg0) {
            for (int i=width-1; i>=0;i--) {
                if (list[start+i] == (Double)arg0)
                    return i;
            }
            return -1;        
        }

        @Override
        public ListIterator<Double> listIterator(int initpos) {
            return new ListIterator<Double>() {
                int pos = start + initpos - 1;
                @Override
                public boolean hasNext() {
                    return pos<end;
                }

                @Override
                public Double next() {
                    return list[++pos];
                }

                @Override
                public boolean hasPrevious() {
                    return pos>end;
                }

                @Override
                public Double previous() {
                    return list[--pos];
                }

                @Override
                public int nextIndex() {
                    return pos+1 -start;
                }

                @Override
                public int previousIndex() {
                    return pos-1-start;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void set(Double value) {
                    list[pos] = value;
                }

                @Override
                public void add(Double arg0) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }

        @Override
        public ListIterator<Double> listIterator() {
            return this.listIterator(-1);
        }

        @Override
        public List<Double> subList(int from, int to) {
            return new RowView(start+from, start+end-1);
        }
        
    }
    
    
    public class RowViewIterator implements ListIterator<RowView> {
        int row;
        
        public RowViewIterator() {
            row = -1;
        }

        public RowViewIterator(int nextrow) {
            row = nextrow-1;
        }
        
        @Override
        public boolean hasNext() {
            return row<rows-1;
        }

        @Override
        public RowView next() {
            return new RowView(++row);
        }

        @Override
        public boolean hasPrevious() {
            return row>0;
        }

        @Override
        public RowView previous() {
            return new RowView(--row);
        }

        @Override
        public int nextIndex() {
            return row+1;
        }

        @Override
        public int previousIndex() {
            return row-1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void set(RowView arg0) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void add(RowView arg0) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    }
            
            
    public DoubleArrayList2D(int width) {
        this(width,1);
    }
    
    
    public DoubleArrayList2D(int width,int rowcapacity) {
        this.width = width;
        list = new double[rowcapacity*width];
    }

    

    
    
    public boolean add(double[] value) {
        ensureCapacity(++rows);
        // transfer the array
        System.arraycopy(value, 0, list, count, width); 
        count+=width;
        return true;
    }

    protected void ensureCapacity(int rows) {
        int space = rows*width;
        if (space<list.length)
            return;
        int step = (list.length / width /10) + 1 * width;
        // make sure we have space
        if (space > count+step) 
            list = java.util.Arrays.copyOf(list, space);
        else
            list = java.util.Arrays.copyOf(list, count + step);
        
    }

    public boolean add(Double[] value) {
        return add(Arrays.stream(value).mapToDouble(Double::doubleValue).toArray());
    }
    
    public boolean add(List<Double> values) {
        ensureCapacity(++rows);
        Iterator<Double> it = values.iterator();
        for (int i= 0; i<width;i++) {
            list[count++]=it.next();
        }
        return true;
    }    
    public void add(int row, double[] values) {
        throw new UnsupportedOperationException("currently not supported");
//        if (row>rows) {
//            set(row, values);
//            return;
//        }
//        ensureCapacity(row);
//        
//        System.arraycopy(values, 0, list, count, width); 
//        count+=width;
    }

    public double set(int row, double[] value) {
        throw new UnsupportedOperationException("currently not supported");
    }
    
    public List<Double> get(int row) {
        return new RowView(row);
    }

    public double[] getDouble(int row) {
        double[] ret = new double[width];
        System.arraycopy(list, row*width, ret, 0, width);
        return ret;
    }    
    
    public double getDouble(int row, int column) {
        
        return list[row*width+column];
    }        
    
    public List<Double> remove(int pos) {
        throw new UnsupportedOperationException("Can't do that at the moment");
    }
    
    

    public int size() {
        return rows;
    }

    public boolean isEmpty() {
        return count==0;
    }

    public boolean contains(Object o) {
        if (o instanceof Double) {
            double c = (Double)o;
            for (int i = 0; i<count;i++) {
                if (list[i] == c)
                    return true;
            }
        }
        return false;
    }
    public Iterator<List<Double>> iterator() {
        return listIterator(0);
    }

    public ListIterator<List<Double>> listIterator() {
        return listIterator(0);
    }
    
    public ListIterator<List<Double>> listIterator(final int nextrow) {
        return new ListIterator<List<Double>>() {
            int row=nextrow-1;


            @Override
            public boolean hasNext() {
                return row<rows-1;
            }

            @Override
            public RowView next() {
                return new RowView(++row);
            }

            @Override
            public boolean hasPrevious() {
                return row>0;
            }

            @Override
            public RowView previous() {
                return new RowView(--row);
            }

            @Override
            public int nextIndex() {
                return row+1;
            }

            @Override
            public int previousIndex() {
                return row-1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void set(List<Double> arg0) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void add(List<Double> arg0) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
    }

    
    @Override
    public Double[][] toArray() {
        Double[][] ret = new Double[rows][];
        int  r = 0;
        for (List<Double> l : this) {
            ret[r]=(Double[]) l.toArray();
        }
        return ret;
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        if (a instanceof Double[][]) {
            if (a.length < rows) {
                return  (T[]) toArray();
            }
            Double[][] ret  = (Double[][]) a;
            int  r = 0;
            for (List<Double> l : this) {
                ret[r]=(Double[]) l.toArray();
            }
        }
        throw new UnsupportedOperationException("Cannot convert double[] to the given type ("+ a.getClass().getSimpleName() + ")");
    }

    public double[][] toDoubleArray() {
        double[][] ret = new double[count][width];
        for (int r=0; r<rows;r++) {
            int rs = r*width;
            for (int c=0; c<width;c++) {
                ret[r][c]=list[rs+c];
            }
        }
        return ret;
    }
    
    public boolean remove(Object o) {
        if (o instanceof Double) {
            Double c = (Double) o;
            for (int i=0; i< count; i++) {
                if (list[i] == c) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        Collection<Double> ci = (Collection<Double>) c;
        for (Double i : ci) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends List<Double>> c) {
        Collection<List<Double>> ci = (Collection<List<Double>>) c;
        for (List<Double> i : ci) {
            if (!add(i))
                return false;
        }
        return true;
    }

    public boolean addAll(DoubleArrayList2D c) {
        return true;
    }

    
    public void clear() {
        count = 0;
    }

    /**
     * returns the actual storage array.
     * <br/>Be careful there are likely more entries in the array as the list supposed to have.
     * These are stand-ins for later points in time.
     * Also the array that is actually stores the data might change (e.g. by adding or removing elements from the list) and the changes to the array would not persist.
     * 
     * <br/></br><b>So be warned...!</b>
     * 
     * <br/></br><b>This is utterly not thread-safe</b>
     */
    public double[] getRawArray() {
        return list;
    }
    
    @Override
    public DoubleArrayList2D clone() {
        DoubleArrayList2D ret = new DoubleArrayList2D(0);
        ret.list = (double[])list.clone();
        ret.count = count;
        return ret;
    }
    
    /**
     * efficient way to add a value to all entries in the list
     * @param a 
     */
    public void addToAll(double a) {
        for (int i=count-1; i>=0; i--)
            list[i]+=a;
    }

    /**
     * efficient way to multiply each value in the list with the given value
     * @param a 
     */
    public void multiplyToAll(double a) {
        for (int i=count-1; i>=0; i--)
            list[i]*=a;
    }
    
    public ArrayList<Double> getColumn(int column) {
        ArrayList<Double> ret = new ArrayList<Double>(this.rows);
        int pos = column;
        while (pos<count){
            ret.add(list[pos]);
            pos+=width;
        }
        return ret;
    }

    public ArrayList<Double>[] toColumns(int column) {
        ArrayList<Double>[] ret = new ArrayList[this.width];
        for (int i = 0; i<width;i++) {
            ret[i] = new ArrayList<Double>(rows);
        }
        int pos = 0;
        while (pos<count){
            int i = pos % width;
            ret[i].add(list[pos++]);
        }
        return ret;
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
    public boolean addAll(int arg0, Collection<? extends List<Double>> arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Double> set(int arg0, List<Double> arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int arg0, List<Double> arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int indexOf(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int lastIndexOf(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<List<Double>> subList(int arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




}
