/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class Pair<T,Y> {
    public T v1;
    public Y v2;

    public Pair(T v1, Y v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    
    public T v1() {
        return v1;
    }

    public Y v2() {
        return v2;
    }

    public Pair() {
    }
    
}
