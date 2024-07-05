/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rappsilber.utils;

import java.io.File;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class JavaUtils {
    public static String findJava() {
        String[] javaexecs = new String[] {
            "java", 
            ".." + File.separator + "bin" + File.separator + "java",
            File.separator + "bin" + File.separator + "java"
        };
        
        final String javaLibraryPath = System.getProperty("sun.boot.library.path") + 
                File.pathSeparator + System.getProperty("java.library.path");
        File javaExeexecutable = null;
        for (String jp : javaLibraryPath.split(File.pathSeparator)) {
            for (String e : javaexecs) {
                String path = jp + File.separator + e;
                javaExeexecutable = new File(path);
                if (! javaExeexecutable.canExecute()) {
                    javaExeexecutable = new File(path + ".EXE");
                }
                if (javaExeexecutable.canExecute()) {
                    return javaExeexecutable.getAbsolutePath();
                }
            }
            
        }
        
        return "java";        
    }
    
    
    public static int getJavaMajorVersion() {
        // get the version string from property
        String version = System.getProperty("java.version");
        
        // there was a change from 1.x.y to x.y
        if(version.startsWith("1.")) {
            // old version string
            version = version.substring(2, 3);
        } else {
            // new version string
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
        
    }
    
}
