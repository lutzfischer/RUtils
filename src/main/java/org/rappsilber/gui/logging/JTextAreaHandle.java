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
package org.rappsilber.gui.logging;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * a logging-handler, that forwards the logging-messages to a JTextArea.
 *
 * @author lfischer
 */
public class JTextAreaHandle extends Handler {
    //the window to which the logging is done

    private JTextArea m_output = null;

    private StringBuffer m_log = new StringBuffer();
    final private LinkedList<LogRecord> records = new LinkedList<>();

    final private LinkedList<LogRecord> newRecords = new LinkedList<>();

    private Formatter formatter = null;

    private Level level = null;

    private int m_maxlogsize = 100000;

    private AtomicBoolean publish = new AtomicBoolean(false);
    
    private long lastOutput = 0;

    TimerTask batchUpdateTask;
    boolean batchTaskRunning = false;
    /**
     * private constructor, preventing initialisation
     *
     * @param out the JTextArea that should display the log
     */
    public JTextAreaHandle(JTextArea out) {
        configure();
        m_output = out;
        
    }

    protected void batchPublish() {
        int cp = m_output.getCaretPosition();
        boolean adjustCP = false;
        boolean moveCPEnd = false;
        if (cp > m_log.length() - 10) {
            adjustCP = true;
            moveCPEnd = true;
        } else {
            if (cp >= 0) {
                adjustCP = true;
            }
        }

        String message;
        records.addAll(newRecords);

        while (records.size() > m_maxlogsize) {

            LogRecord todelete = records.getFirst();
            if (todelete.getLevel().intValue() >= getLevel().intValue()) {
                LogRecord d = records.removeFirst();
                message = getFormatter().format(d);
                m_log.delete(0, message.length());
                cp -= message.length();
            }
        }
        for (LogRecord record : newRecords) {
            //check if the record is loggable
            if (!isLoggable(record)) {
                continue;
            }
            try {
                message = getFormatter().format(record);
            } catch (Exception e) {
                reportError(null, e, ErrorManager.FORMAT_FAILURE);
                continue;
            }
            m_log.append(message);
        }
        newRecords.clear();

        try {
            final boolean adjust = adjustCP;
            final boolean moveEnd  =moveCPEnd;
            final int c = cp;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    m_output.setText(m_log.toString());
                    if (adjust) {
                        m_output.select(Integer.MAX_VALUE, Integer.MAX_VALUE);
                        if (moveEnd) {
                            m_output.setCaretPosition(m_output.getText().length());
                        } else {
                            m_output.setCaretPosition(c);
                        }
                    }
                }
            });

        } catch (Exception ex) {
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }
    }

    /**
     * The getInstance method returns the singleton instance of the
     * WindowHandler object It is synchronised to prevent two threads trying to
     * create an instance simultaneously. @ return WindowHandler object
     */
//  public static synchronized JTextAreaHandle getInstance() {
//
//    if (handler == null) {
//      handler = new WindowHandler();
//    }
//    return handler;
//  }
    /**
     * This method loads the configuration properties from the JDK level
     * configuration file with the help of the LogManager class. It then sets
     * its level, filter and formatter properties.
     */
    private void configure() {
        LogManager manager = LogManager.getLogManager();
        String className = this.getClass().getName();
        String level = manager.getProperty(className + ".level");
        String filter = manager.getProperty(className + ".filter");
        String formatter = manager.getProperty(className + ".formatter");

        //accessing super class methods to set the parameters
        setLevel(level != null ? Level.parse(level) : Level.INFO);
        //setFilter(makeFilter(filter));
        setFormatter(makeFormatter(formatter));

    }

//  /**
//   * private method constructing a Filter object with the filter name.
//   *
//   * @param filterName
//   *            the name of the filter
//   * @return the Filter object
//   */
//  private Filter makeFilter(String filterName) {
//    Class c = null;
//    Filter f = null;
//    try {
//      c = Class.forName(filterName);
//      f = (Filter) c.newInstance();
//    } catch (Exception e) {
//      System.out.println("There was a problem to load the filter class: "
//          + filterName);
//    }
//    return f;
//  }
    /**
     * private method creating a Formatter object with the formatter name. If no
     * name is specified, it returns a SimpleFormatter object
     *
     * @param formatterName the name of the formatter
     * @return Formatter object
     */
    private Formatter makeFormatter(String formatterName) {
        Class c = null;
        Formatter f = null;

        try {
            c = Class.forName(formatterName);
            f = (Formatter) c.newInstance();
        } catch (Exception e) {
            f = new SimpleFormatter();
        }
        return f;
    }

    /**
     * This is the overridden publish method of the abstract super class
     * Handler. This method writes the logging information to the associated
     * Java window. This method is synchronized to make it thread-safe. In case
     * there is a problem, it reports the problem with the ErrorManager, only
     * once and silently ignores the others.
     *
     * @record the LogRecord object
     *
     */
    public synchronized void publish(LogRecord record) {
        String message = null;
        newRecords.add(record);
        publish.set(true);
        long current = Calendar.getInstance().getTimeInMillis();
        if (isLoggable(record)) {
            if (current - lastOutput > 1000 ) {
                lastOutput = current;
                batchPublish();
            } else {
                if (!batchTaskRunning) {
                    batchTaskRunning = true;
                    final Timer logtimer = new Timer("JTAh - LOG batch out", true);
                    TimerTask batchUpdateTask = new TimerTask() {
                        @Override
                        public void run() {
                            synchronized(JTextAreaHandle.this) {
                                lastOutput = Calendar.getInstance().getTimeInMillis();
                                batchPublish();
                                batchTaskRunning = false;
                                logtimer.cancel();
                            }
                        }
                    };
                    logtimer.schedule(batchUpdateTask, 1000-(current-lastOutput));
                }
            }
        }
        

    }

    @Override
    public synchronized void setLevel(Level newLevel) throws SecurityException {
        super.setLevel(newLevel); //To change body of generated methods, choose Tools | Templates.
        m_log.setLength(0);
        for (LogRecord r : records) {
            if (isLoggable(r)) {
                m_log.append(getFormatter().format(r));
            }
        }
        if (m_output != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    m_output.setText(m_log.toString());
                    m_output.select(Integer.MAX_VALUE, Integer.MAX_VALUE);
                }
            });
        }
    }

    public void close() {
    }

    public void flush() {
    }
}
