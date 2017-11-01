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
package org.rappsilber.gui.components.memory;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author lfischer
 */
public class Memory extends javax.swing.JPanel {

    
    java.util.Timer m_scanTimer = new java.util.Timer(true);
    private int m_timeout = 600;
    Runtime runtime = Runtime.getRuntime();
    
    static public String toHuman(double n) {
        String u = "B";
        if (n > 1024) {
            n/=1024;
            u = "KB";
        }
        if (n > 1024) {
            n/=1024;
            u = "MB";
        }
        if (n > 1024) {
            n/=1024;
            u = "GB";
        }
        if (n > 1024) {
            n/=1024;
            u = "TB";
        }
        
        if (n>10)
            return String.format("%.0f" + u, n);
        
        if (Math.abs(Math.round(n) - n) >=0.1) {
            return String.format("%.1f" + u, n);
        } 
        
        return String.format("%.0f" + u, n);
    }
    
    protected class ScanTask extends TimerTask {
        AtomicBoolean running = new AtomicBoolean(false);
        @Override
        public void run() {
            if (running.compareAndSet(false, true)) {

                double fm = runtime.freeMemory();
                String fmu = "B";
                double mm = runtime.maxMemory();
                double tm = runtime.totalMemory();
                double um = tm-fm;
                
                   
                txtMemory.setText("Used: " + toHuman(um) + " of " + toHuman(mm) + "  (Free:" + toHuman(fm) + " Total:" + toHuman(tm) + " Max:"+ toHuman(mm) +")");
                running.set(false);
            }
        }
        
    }
    
    /**
     * Creates new form Memory
     */
    public Memory() {
        initComponents();
        m_scanTimer.scheduleAtFixedRate(new ScanTask(), 1000, 1000);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtMemory = new javax.swing.JTextField();
        gc = new javax.swing.JButton();

        txtMemory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemoryActionPerformed(evt);
            }
        });

        gc.setText("gc");
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
                .addComponent(txtMemory, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gc))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtMemory)
                .addComponent(gc, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtMemoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemoryActionPerformed

    private void gcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gcActionPerformed
        System.gc();
    }//GEN-LAST:event_gcActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gc;
    private javax.swing.JTextField txtMemory;
    // End of variables declaration//GEN-END:variables
}
