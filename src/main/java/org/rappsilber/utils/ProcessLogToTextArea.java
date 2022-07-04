/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author lfischer
 */
public class ProcessLogToTextArea implements ProcessLauncher.OutputListener{

    private boolean toSTDOUT = true;
    private PrintWriter logOut = null;
    private StringBuffer stdout = new StringBuffer(1000);
    private JTextArea out;

    public ProcessLogToTextArea() {
    }
    
    public ProcessLogToTextArea(JTextArea out) {
        this.out = out;
    }
    
    public void standardOutput(final String s) {
        if (out != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    out.append(s);
                }
            });
        }
    }

    public void errorOutput(final String s) {
        if (out != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    out.append(s);
                }
            });
        }
    }
    
    public PrintWriter getOutput() {
        return logOut;
    }
    
    

    
    public void close() {
    }
    
    public synchronized void standardOutput(final char[] output) {
        if (out != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    out.append(new String(output));
                }
            });
        }
    }

    public synchronized void errorOutput(final char[] output) {
        if (out != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    out.append(new String(output));
                }
            });
        }
    }
    
}
