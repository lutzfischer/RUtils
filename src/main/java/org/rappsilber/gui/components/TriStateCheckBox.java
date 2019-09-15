/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.gui.components;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import static javax.swing.DefaultButtonModel.PRESSED;
import static javax.swing.DefaultButtonModel.SELECTED;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

/**
 * Extends a JCheckBox to have  a third state (null)
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class TriStateCheckBox extends JCheckBox{
    boolean mid_is_seelcted = true;
    Boolean state = null;
    Icon icon = UIManager.getIcon("CheckBox.icon");
    Icon nullIcon = new NullIcon();
    
    private class NullIcon implements Icon {
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            icon.paintIcon(c, g, x, y);

            int w = getIconWidth();
            int h = getIconHeight();
            g.setColor(c.isEnabled() ? new Color(51, 51, 51) : new Color(122, 138, 153));
            g.fillRect(x+4, y+4, w-8, h-8);

            if (!c.isEnabled()) return;
            g.setColor(new Color(81, 81, 81));
            g.drawRect(x+4, y+4, w-9, h-9);
            
        }

        @Override
        public int getIconWidth() {
            return icon.getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return icon.getIconHeight();
        }
        
    }
  

    private class TristateButtonModel extends JToggleButton.ToggleButtonModel {

        
        // shamlessly ripped of from ToggleButtonModel
        @Override
        public void setPressed(boolean b) {
            if ((isPressed() == b) || !isEnabled()) {
                return;
            }

            if (b == false && isArmed()) {
                if (getSelectionState() == Boolean.FALSE) {
                    setSelectionState((Boolean)null);
                    setSelected(mid_is_seelcted);
                } else if (getSelectionState() == null) {
                    setSelectionState(Boolean.TRUE);
                    setSelected(true);
                } else {
                    setSelectionState(Boolean.FALSE);
                    setSelected(false);
                }
            }

            if (b) {
                stateMask |= PRESSED;
            } else {
                stateMask &= ~PRESSED;
            }

            fireStateChanged();

            if(!isPressed() && isArmed()) {
                int modifiers = 0;
                AWTEvent currentEvent = EventQueue.getCurrentEvent();
                if (currentEvent instanceof InputEvent) {
                    modifiers = ((InputEvent)currentEvent).getModifiers();
                } else if (currentEvent instanceof ActionEvent) {
                    modifiers = ((ActionEvent)currentEvent).getModifiers();
                }
                fireActionPerformed(
                    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                    getActionCommand(),
                                    EventQueue.getMostRecentEventTime(),
                                    modifiers));
            }

        }
            
        
        public void setSelected(Boolean b) {
            ButtonGroup group = getGroup();
            if (group != null) {
                // use the group model instead
                group.setSelected(this, b);
                b = group.isSelected(this);
            }

            if (state == b) {
                return;
            }

            if (b) {
                stateMask |= SELECTED;
            } else {
                stateMask &= ~SELECTED;
            }

            // Send ChangeEvent
            fireStateChanged();

            // Send ItemEvent
            fireItemStateChanged(
                    new ItemEvent(this,
                                  ItemEvent.ITEM_STATE_CHANGED,
                                  this,
                                  this.isSelected() ?  ItemEvent.SELECTED : ItemEvent.DESELECTED));

        }        
    }
    
    
    
    public TriStateCheckBox() {
        super();
        putClientProperty("SelectionState", (Boolean)null);
//        setIcon(new TriStateIcon());
        
        setModel(new TristateButtonModel());
    }

    
    @Override
    public void paint(Graphics g) {
        if (state == null) {
            setIcon(nullIcon);
        }else {
            setIcon(null);
        }
        super.paint(g);
    }    
    
    public Boolean getSelectionState() {
        return this.state;
    }

    @Override
    public void setSelected(boolean b) {
        throw new UnsupportedOperationException("don't call this");
    }
    
    public void setSelected(Boolean b) {
        ((TristateButtonModel)model).setSelected(b);
    }
    

    public void setSelectionState(Boolean state) {
        this.state = state;
        if (state == null) {
            super.setSelected(mid_is_seelcted);
        } else {
            super.setSelected((boolean)state);
        }
    }
    
}
