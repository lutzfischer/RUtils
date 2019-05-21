/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.ArrayList;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class ArrayListCoSwap<T> extends ArrayList<T> implements Swap{

    @Override
    public void swap(int pos1, int pos2) {
        T dummy = get(pos1);
        set(pos1,get(pos2));
        set(pos2,dummy);
    }
    
}
