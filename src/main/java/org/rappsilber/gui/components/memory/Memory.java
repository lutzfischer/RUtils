/* 
 * Copyright 2016 Lutz Fischer <l.fischer@ed.ac.uk>.
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
package org.rappsilber.gui.components.memory;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.rappsilber.gui.components.StackTraceMonitor;
import org.rappsilber.utils.ObjectWrapper;
import org.rappsilber.utils.RArrayUtils;
import org.rappsilber.utils.StringUtils;
import org.rappsilber.utils.UStackTraces;

/**
 *
 * @author Lutz Fischer <l.fischer@ed.ac.uk>
 */
public class Memory extends javax.swing.JPanel {

    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    public static final String PROP_SHOWLOGBUTTON = "showLogButton";
    public static final String PROP_SHOWAUTOGCBUTTON = "showAutoGCButton";
    public static final String PROP_SHOWGCBUTTON = "showGCButton";

    
    java.util.Timer m_scanTimer = new java.util.Timer("Timer - FreeMemoryScan",true);
    private int m_timeout = 600;
    Runtime runtime = Runtime.getRuntime();
    private boolean showLogButton = true;
    private boolean showAutoGCButton = true;
    private boolean showGCButton = true;
    private StackTraceMonitor stacktracemonitor;
    
    //private boolean agc = false;
    
    protected class ScanTask extends TimerTask {
        AtomicBoolean running = new AtomicBoolean(false);
        double recentMinFreeMem = 0;
        double recentMaxFreeMem = 0;
        LinkedList<Double> recent = new LinkedList<>();
        int maxRecent=100;
        int updateSteps=10;
        int logMemory = 0;
        int didgc = 0;
        int step=0;
        
        @Override
        public void run() {
            if (running.compareAndSet(false, true)) {
                try {
                    double fm = runtime.freeMemory();
                    String fmu = "B";
                    double mm = runtime.maxMemory();
                    double tm = runtime.totalMemory();
                    double um = tm-fm;
                    recent.add(um);
                    if (recent.size()>maxRecent) {
                        recent.removeFirst();
                    }
                    if (++step==updateSteps) {
                        step=0;
                        ObjectWrapper<Double> min= new ObjectWrapper<>();
                        ObjectWrapper<Double> max = new ObjectWrapper<>();
                        ObjectWrapper<Double> average = new ObjectWrapper<>();
                        RArrayUtils.minmaxaverage(recent,min,max,average);
                        String message = "Used: " + StringUtils.toHuman(um) + " of " + StringUtils.toHuman(mm) + "  (Free:" + StringUtils.toHuman(fm) + " Total:" + StringUtils.toHuman(tm) + " Max:"+ StringUtils.toHuman(mm) +") (recent used:[" +  StringUtils.toHuman(min.value) +".."+ StringUtils.toHuman(average.value) +".." + StringUtils.toHuman(max.value) +"])";
                        if (mckLog.isSelected()) {
                            if (logMemory++ % 60 == 0 ) {
                                Logger.getLogger(Memory.class.getName()).log(Level.INFO,message);
                            }
                        } else 
                            logMemory = 0;
                        if (txtMemory!=null) {
                            txtMemory.setText(message);
                        }
                        if (mckAGC.isSelected() && mm-um < 10*1024*1024 && didgc== 0) {
                            Logger.getLogger(Memory.class.getName()).log(Level.INFO,"AutoGC triggered");
                            message = "Used: " + StringUtils.toHuman(um) + " of " + StringUtils.toHuman(mm) + "  (Free:" + StringUtils.toHuman(fm) + " Total:" + StringUtils.toHuman(tm) + " Max:"+ StringUtils.toHuman(mm) +")";
                            Logger.getLogger(Memory.class.getName()).log(Level.INFO,"Memory before GC:" + message);

                            System.gc();
                            System.gc();

                            fm = runtime.freeMemory();
                            mm = runtime.maxMemory();
                            tm = runtime.totalMemory();
                            um = tm-fm;
                            message = "Used: " + StringUtils.toHuman(um) + " of " + StringUtils.toHuman(mm) + "  (Free:" + StringUtils.toHuman(fm) + " Total:" + StringUtils.toHuman(tm) + " Max:"+ StringUtils.toHuman(mm) +")";
                            Logger.getLogger(Memory.class.getName()).log(Level.INFO,"Memory after GC:" + message);
                            didgc=100;
                        } else if (didgc>0) {
                            didgc--;
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(Memory.class.getName()).log(Level.INFO,"Error during memory display:",e);
                }
                running.set(false);
                
            }
        }
        
    }
    
    /**
     * Creates new form Memory
     */
    public Memory() {
        initComponents();
        m_scanTimer.scheduleAtFixedRate(new ScanTask(), 100, 100);
        this.addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                Component c =  Memory.this;
                while (c.getParent() != null) {
                    c = c.getParent();
                }
                if (c instanceof java.awt.Window) {
                    ((java.awt.Window) c).addWindowListener(new WindowListener() {

                        public void windowOpened(WindowEvent e) {
                            m_scanTimer.scheduleAtFixedRate(new ScanTask(), 1000, 1000);
                        }

                        public void windowClosing(WindowEvent e) {
                        }

                        public void windowClosed(WindowEvent e) {
                            m_scanTimer.cancel();
                        }

                        public void windowIconified(WindowEvent e) {
                        }

                        public void windowDeiconified(WindowEvent e) {
                        }

                        public void windowActivated(WindowEvent e) {
                        }

                        public void windowDeactivated(WindowEvent e) {
                        }
                    });
                }
            }

            public void ancestorRemoved(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
                
            }
        });
    }
    
    @Override
    protected void finalize() {
        m_scanTimer.cancel();
    }

        /**
     * @return the showLogButton
     */
    public boolean isShowLogButton() {
        return showLogButton;
    }

    /**
     * @param showLogButton the showLogButton to set
     */
    public void setShowLogButton(boolean showLogButton) {
        boolean oldShowLogButton = this.showLogButton;
        this.showLogButton = showLogButton;
        propertyChangeSupport.firePropertyChange(PROP_SHOWLOGBUTTON, oldShowLogButton, showLogButton);
    }

    /**
     * @return the showAutoGCButton
     */
    public boolean isShowAutoGCButton() {
        return showAutoGCButton;
    }

    /**
     * @param showAutoGCButton the showAutoGCButton to set
     */
    public void setShowAutoGCButton(boolean showAutoGCButton) {
        boolean oldShowAutoGCButton = this.showAutoGCButton;
        this.showAutoGCButton = showAutoGCButton;
        propertyChangeSupport.firePropertyChange(PROP_SHOWAUTOGCBUTTON, oldShowAutoGCButton, showAutoGCButton);
    }

    /**
     * @return the showGCButton
     */
    public boolean isShowGCButton() {
        return showGCButton;
    }

    /**
     * @param showGCButton the showGCButton to set
     */
    public void setShowGCButton(boolean showGCButton) {
        boolean oldShowGCButton = this.showGCButton;
        this.showGCButton = showGCButton;
        propertyChangeSupport.firePropertyChange(PROP_SHOWGCBUTTON, oldShowGCButton, showGCButton);
        this.gc.setVisible(showGCButton);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        utilsmenu = new javax.swing.JMenu();
        mGC = new javax.swing.JMenuItem();
        mckLog = new javax.swing.JCheckBoxMenuItem();
        mckAGC = new javax.swing.JCheckBoxMenuItem();
        mStackTraces = new javax.swing.JMenuItem();
        txtMemory = new javax.swing.JTextField();
        gc = new javax.swing.JButton();

        utilsmenu.setText("jMenu1");

        mGC.setText("Garbage Collection");
        mGC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mGCActionPerformed(evt);
            }
        });
        utilsmenu.add(mGC);

        mckLog.setText("Log Memory Usage");
        utilsmenu.add(mckLog);

        mckAGC.setText("Automatic GC on low memory");
        utilsmenu.add(mckAGC);

        mStackTraces.setText("log statck traces");
        mStackTraces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mStackTracesActionPerformed(evt);
            }
        });
        utilsmenu.add(mStackTraces);

        gc.setText("...");
        gc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtMemory, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gc)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMemory)
                    .addComponent(gc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void gcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gcActionPerformed
//        utilsmenu.setVisible(true);
        utilsmenu.getPopupMenu().show(this, gc.getX(), gc.getY());
//        doGC();        
                        
    }//GEN-LAST:event_gcActionPerformed

    protected void doGC() {
        double fm = runtime.freeMemory();
        double mm = runtime.maxMemory();
        double tm = runtime.totalMemory();
        double um = tm-fm;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,"GC triggered");
        String message = "Used: " + StringUtils.toHuman(um) + " of " + StringUtils.toHuman(mm) + "  (Free:" + StringUtils.toHuman(fm) + " Total:" + StringUtils.toHuman(tm) + " Max:"+ StringUtils.toHuman(mm) +")";
        Logger.getLogger(Memory.class.getName()).log(Level.INFO,"Memory before GC:" + message);
        System.gc();
        System.gc();
        
        fm = runtime.freeMemory();
        mm = runtime.maxMemory();
        tm = runtime.totalMemory();
        um = tm-fm;
        message = "Used: " + StringUtils.toHuman(um) + " of " + StringUtils.toHuman(mm) + "  (Free:" + StringUtils.toHuman(fm) + " Total:" + StringUtils.toHuman(tm) + " Max:"+ StringUtils.toHuman(mm) +")";
        Logger.getLogger(Memory.class.getName()).log(Level.INFO,"Memory after GC:" + message);
    }

    private void mGCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mGCActionPerformed
        doGC();
    }//GEN-LAST:event_mGCActionPerformed

    private void mStackTracesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mStackTracesActionPerformed
        UStackTraces.logStackTraces(Level.SEVERE);
        UStackTraces.getStackTraces().toString();
        if (stacktracemonitor == null) {
            stacktracemonitor = new StackTraceMonitor();
        }
        stacktracemonitor.setVisible(true);
    }//GEN-LAST:event_mStackTracesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gc;
    private javax.swing.JMenuItem mGC;
    private javax.swing.JMenuItem mStackTraces;
    private javax.swing.JCheckBoxMenuItem mckAGC;
    private javax.swing.JCheckBoxMenuItem mckLog;
    private javax.swing.JTextField txtMemory;
    private javax.swing.JMenu utilsmenu;
    // End of variables declaration//GEN-END:variables
}
