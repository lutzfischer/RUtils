/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class UStackTraces {
    public static void logStackTraces(Level level) {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        logStackTraces(level, tg);
    }

    public static void logStackTraces(Level level,ThreadGroup tg) {
        StringBuilder sb = getStackTraces(tg);
        Logger.getLogger(UStackTraces.class.getName()).log(level, sb.toString());
    }

    public static StringBuilder getStackTraces() {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        return getStackTraces(tg);
    }

    public static StringBuilder getStackTraces(ThreadGroup tg) {
        Thread[] active = new Thread[tg.activeCount()*100];
        tg.enumerate(active, true);
        StringBuilder sb = new StringBuilder();
        for (Thread t : active) {
            if (t != null) {
                try {
                    sb.append("\n--------------------------\n");
                    sb.append("--- Thread stack-trace ---\n");
                    sb.append("--------------------------\n");
                    sb.append("--- " + t.getId() + " : " + t.getName()+"\n");
                    if (t.isDaemon())
                        sb.append("--- DAEMON-THREAD \n");
                    sb.append(RArrayUtils.toString(t.getStackTrace(), "\n"));
                    sb.append("\n");

                } catch (SecurityException se) {
                    Logger.getLogger(UStackTraces.class.getName()).log(Level.SEVERE, "Error:", se);
                    System.err.println("could not get a stacktrace");
                }
            }
        }
        return sb;
    }
    
}
