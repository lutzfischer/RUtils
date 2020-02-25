/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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

    public static StringBuilder getStackTraces(long id, boolean excludeDaemon) {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        return getStackTraces(tg, id, null, excludeDaemon);
    }

    public static StringBuilder getStackTraces(long id, String namePattern, boolean excludeDaemon) {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        return getStackTraces(tg, id, namePattern, excludeDaemon);
    }

    public static StringBuilder getStackTraces(String name, boolean excludeDaemon) {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        return getStackTraces(tg, -1, name, excludeDaemon);
    }

    public static StringBuilder getStackTraces(ThreadGroup tg) {
        return getStackTraces(tg, -1, null, false);
    }

    public static ArrayList<Long> getThreadIds() {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        return getThreadIds(tg);
    }

    public static ArrayList<Long> getThreadIds(ThreadGroup tg) {
        ArrayList<Long> ret = new ArrayList<>();
        Thread[] active = new Thread[tg.activeCount()*100];
        tg.enumerate(active, true);
        for (Thread t : active) {
            if (t != null) {
                ret.add(t.getId());
            }
        }
        return ret;
    }

    public static StringBuilder getStackTraces(ThreadGroup tg, long id, String namePattern, boolean excludeDaemon) {
        Thread[] active = new Thread[tg.activeCount()*100];
        tg.enumerate(active, true);
        StringBuilder sb = new StringBuilder();
        Pattern p = null;
        if (namePattern != null && !namePattern.isBlank()) {
                
            
            if (namePattern.toLowerCase().contentEquals(namePattern))
                p = Pattern.compile(namePattern,Pattern.CASE_INSENSITIVE);
            else
                p = Pattern.compile(namePattern);
            
        }
        
        for (Thread t : active) {
            if (t != null && (id<0 || t.getId() == id) && (p == null || p.matcher(t.getName()).find()) && !(excludeDaemon  && t.isDaemon())) {
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
