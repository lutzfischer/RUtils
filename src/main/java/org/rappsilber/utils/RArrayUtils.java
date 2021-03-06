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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author lfischer
 */
public abstract class RArrayUtils {


    public static String toString(Collection c, String delim) {
        if (c == null || c.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : c) {
            sb.append(delim);
            sb.append(o == null? "" : o.toString());
        }
        return sb.substring(delim.length());
    }

    public static <T extends Number>  String toString(Collection<T> a, String delim, NumberFormat format) {
        if (a == null || a.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (T o : a ) {
            sb.append(delim);
            sb.append(o == null ? "" : format.format(o));
        }
        return sb.substring(delim.length());
    }
    
    public static String toString(int[] a, String delim) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(Integer.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(Integer.toString(a[i]));
        }
        return sb.toString();
    }

    
    public static String[] toStringArray(Object[] a) {
        if (a == null || a.length == 0) {
            return new String[0];
        }
        String[] ret = new String[a.length];
        for (int i = 0; i<a.length; i++) {
            ret[i]=a[i].toString();
        }
        return ret;
    }
    
    public static String toString(int[] a, String delim, NumberFormat format) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(Integer.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(format.format(a[i]));
        }
        return sb.toString();
    }
    
    public static String toString(double[] a, String delim) {
        StringBuilder sb = new StringBuilder(Double.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(Double.toString(a[i]));
        }
        return sb.toString();
    }

    public static String toString(double[] a, String delim, NumberFormat format) {
        StringBuilder sb = new StringBuilder(Double.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(format.format(a[i]));
        }
        return sb.toString();
    }
    
    public static String toString(long[] a, String delim) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(Long.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(Long.toString(a[i]));
        }
        return sb.toString();
    }

    public static String toString(long[] a, String delim, NumberFormat format) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(Long.toString(a[0]));
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(format.format(a[i]));
        }
        return sb.toString();
    }

    public static <T extends Number>  String toString(T[] a, String delim, NumberFormat format) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(a[0].toString());
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(a[i] == null ? "" : format.format(a[i]));
        }
        return sb.toString();
    }

    
    public static <T> String toString(T[] a, String delim) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(a[0].toString());
        for (int i = 1; i<a.length; i++) {
            sb.append(delim);
            sb.append(a[i] == null ? "" : a[i].toString());
        }
        return sb.toString();
    }

    
    public static <T> String toStringNoNull(T[] a, String delim) {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<a.length; i++) {
            if  (a[i] != null) {
                sb.append(a[i].toString());
                sb.append(delim);
            }
        }
        return sb.substring(0, sb.length()-1);
    }
    
    public static String toString(ArrayList a, String delim) {
        if (a == null || a.isEmpty()) {
            return "";
        }

        if (a.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder(a.get(0).toString());
        for (int i = 1; i<a.size(); i++) {
            sb.append(delim);
            Object o = a.get(i);
            sb.append(o == null? "": o.toString());
        }
        return sb.toString();
    }

    
    public static String toString(Iterable a, String delim) {
        Iterator ai = a.iterator();
        if (!ai.hasNext())
            return "";
        StringBuilder sb = new StringBuilder(ai.next().toString());
        while (ai.hasNext()) {
            sb.append(delim);
            Object o = ai.next();
            sb.append(o == null? "":o.toString());
        }
        return sb.toString();
    }
    
    /**
     * Creates a static collection based on the provided array.
     * The Collection does not permit any changes to the data.
     * @param <T>
     * @param list
     * @return 
     */
    public static <T> Collection<T> toCollection(final T[] list) {
        return new Collection<T>() {

            public int size() {
                return list.length;
            }

            public boolean isEmpty() {
                return list.length == 0;
            }

            public boolean contains(Object o) {
                for (T e:list) {
                    if (o.equals(e))
                        return true;
                }
                return false;
            }

            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int pos = 0;
                    public boolean hasNext() {
                        return pos<list.length;
                    }

                    public T next() {
                        return list[pos++];
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
            }

            public Object[] toArray() {
                return list;
            }

            public <E> E[] toArray(E[] a) {
                if (a.getClass() == list.getClass()) {
                    if (a.length >= list.length) {
                        System.arraycopy(list, 0, a, 0, list.length);
                        return a;
                    } else 
                        return (E[]) list;
                } else {
                    throw new UnsupportedOperationException("Array-types incompatible."); //To change body of generated methods, choose Tools | Templates.
                }
            }

            public boolean add(T e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean containsAll(Collection<?> c) {
                for (Object o:c) {
                    if (!contains(o))
                        return false;
                }
                return true;
            }

            public boolean addAll(Collection<? extends T> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void clear() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    public static Collection<Integer> toCollection(final int[] list) {
        return new Collection<Integer>() {

            public int size() {
                return list.length;
            }

            public boolean isEmpty() {
                return list.length == 0;
            }

            public boolean contains(Object o) {
                for (Integer e:list) {
                    if (o.equals(e))
                        return true;
                }
                return false;
            }

            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    int pos = 0;
                    public boolean hasNext() {
                        return pos<list.length;
                    }

                    public Integer next() {
                        return list[pos++];
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
            }

            public Integer[] toArray() {
                Integer[] ret = new Integer[list.length];
                for (int i = 0; i<ret.length; i++)
                    ret[i] = list[i];
                return ret;
            }

            public <E> E[] toArray(E[] a) {
                if (a.getClass() == Integer[].class) {
                    if (a.length >= list.length) {
                        System.arraycopy(toArray(), 0, a, 0, list.length);
                        return a;
                    } else 
                        return (E[]) toArray();
                } else {
                    throw new UnsupportedOperationException("Array-types incompatible."); //To change body of generated methods, choose Tools | Templates.
                }
            }

            public boolean add(Integer e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean containsAll(Collection<?> c) {
                for (Object o:c) {
                    if (!contains(o))
                        return false;
                }
                return true;
            }

            public boolean addAll(Collection<? extends Integer> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void clear() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    
    public double max(double[] values) {
        int i=0;
        double max = Double.NaN;
        // find the first actuall number
        for (;!Double.isNaN(values[i]);i++);
        if (i<values.length) {
            max = values[i];
        }
        for (++i;i<values.length;i++) {
            if (!Double.isNaN(values[i])&& values[i] > max) {
                max=values[i];
            }
        }
        return max;
    }

    public int max(int[] values) {
        int i=0;
        int max = Integer.MIN_VALUE;
        for (;i<values.length;i++) {
            if (values[i] > max) {
                max=values[i];
            }
        }
        return max;
    }

    public <T extends Comparable<T>> T  max(T[] values) {
        T max = values[0];
        for (int i=1;i<values.length;i++) {
            if (values[i].compareTo(max)>0) {
                max=values[i];
            }
        }
        return max;
    }

    public double min(double[] values) {
        int i=0;
        double min = Double.NaN;
        // find the first actuall number
        for (;!Double.isNaN(values[i]);i++);
        if (i>values.length) {
            min = values[i];
        }
        for (++i;i<values.length;i++) {
            if (!Double.isNaN(values[i])&& values[i] < min) {
                min=values[i];
            }
        }
        return min;
    }

    public int min(int[] values) {
        int i=0;
        int min = values[0];
        for (;i<values.length;i++) {
            if (values[i] < min) {
                min=values[i];
            }
        }
        return min;
    }

    public <T extends Comparable<T>> T  min(T[] values) {
        T min = values[0];
        for (int i=1;i<values.length;i++) {
            if (values[i].compareTo(min)<0) {
                min=values[i];
            }
        }
        return min;
    }
    
    
    public int min(int v1,int ... v2) {
        return Math.min(v1,min(v2));
    }
    
    public int max(int v1,int ... v2) {
        return Math.max(v1,max(v2));
    }


    public static <T extends Comparable<T>> T  min(Collection<T> values) {
        T min = null;
        Iterator<T> it = values.iterator();
        if (it.hasNext()) {
            min = it.next();
            while (it.hasNext()) {
                T n = it.next();
                if (n.compareTo(min)<0) {
                    min=n;
                }
            }
        }
        return min;
    }

    public static <T extends Comparable<T>> T  max(Collection<T> values) {
        T max = null;
        Iterator<T> it = values.iterator();
        if (it.hasNext()) {
            max = it.next();
            while (it.hasNext()) {
                T n = it.next();
                if (n.compareTo(max)>0) {
                    max=n;
                }
            }
        }
        return max;
    }
    
    public static <T extends Comparable<T>> void  minmax(Collection<T> values, ObjectWrapper<T> min, ObjectWrapper<T> max) {
        min.value = null;
        max.value = null;
        Iterator<T> it = values.iterator();
        if (it.hasNext()) {
            min.value = it.next();
            max.value = min.value;
            while (it.hasNext()) {
                T n = it.next();
                if (n.compareTo(min.value)<0) {
                    min.value=n;
                }
                if (n.compareTo(max.value)>0) {
                    max.value=n;
                }
            }
        }
    }
    
    public static <T extends Number & Comparable<T>> void  minmaxaverage(Collection<T> values, ObjectWrapper<T> min, ObjectWrapper<T> max,ObjectWrapper<Double> average) {
        min.value = null;
        max.value = null;
        double sum =0;
        Iterator<T> it = values.iterator();
        if (it.hasNext()) {
            min.value = it.next();
            max.value = min.value;
            sum=min.value.doubleValue();
            while (it.hasNext()) {
                T n = it.next();
                if (n.compareTo(min.value)<0) {
                    min.value=n;
                }
                if (n.compareTo(max.value)>0) {
                    max.value=n;
                }
                sum+=n.doubleValue();
            }
        }
        average.value = sum / values.size();
    }
    
}
