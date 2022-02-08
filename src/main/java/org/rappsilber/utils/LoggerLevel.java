/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Provides arbitrary functions related to {@link java.util.logging.Level java.util.logging.Level}
 * 
 * currently the only method is {@link #getLevel getLevel}
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class LoggerLevel {
    /**
     * Return the effective level of the logger.
     * 
     * <p>If the logger has no level assigned it will check the chain of parent-loggers for the effective level.</p>
     * @param logger
     * @return 
     */
    public static Level getLevel(Logger logger) {
        // get the level of the logger
        Level level = logger.getLevel();
        
        while (level  == null && logger.getParent() != null) {
            logger = logger.getParent();
            level = logger.getLevel();
        }
        return level;
    }
}
