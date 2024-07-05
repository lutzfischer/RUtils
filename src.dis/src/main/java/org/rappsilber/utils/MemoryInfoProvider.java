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
public interface MemoryInfoProvider {
    long getFreeMem();

    long getMaxMem();
    
    long geTotalMem();

    void initiateGC();
    
}
