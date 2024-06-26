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

/*
 * FileBrowser.java
 *
 * Created on 06-Nov-2009, 15:27:17
 */

package org.rappsilber.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import org.rappsilber.config.LocalProperties;
import org.rappsilber.gui.GetFile;
import org.rappsilber.utils.RArrayUtils;

/**
 *
 * @author lfischer
 */
public class FileBrowser extends javax.swing.JPanel {
    private static final long serialVersionUID = 676003385063269187L;
    
    /** Default property key to store the last accessed folder */
    static final String DefaultLocalPropertyKey = "LastAccessedFolder";

    ArrayList<java.awt.event.ActionListener> m_actionlisteners = new ArrayList<ActionListener>();
    /**  list of files get filtered to these extensions */
    private String[] m_extensions = null;
    /** what to show as description for these file extensions */
    private String m_description = "Files";
    /** list of files selected */
    File[]    m_file;
    /** used to store the last used folder */
    private String m_LocalPropertyKey = DefaultLocalPropertyKey;

    /** automatically add the extension to selected files */
    private boolean m_autoAddExtension = true;
    /** what extension to add - if none is given take from the list of filtered extensions*/
    private String m_autoAddDefaultExtension = null;
    /** show load button or save button*/
    private boolean m_doLoad = true;
    /** only list directories */
    protected boolean m_directoryOnly = false;
    /** enable selection of several files */
    private boolean m_multipleFiles = false;

    /** Creates new form FileBrowser */
    public FileBrowser() {
        initComponents();
    }
    
    @Override
    public void setEnabled(boolean e) {
        super.setEnabled(e);
        btnSelect.setEnabled(e);
        txtFilePath.setEnabled(e);
    }

    /** call all action listeners */
    protected void doActionPerformed() {
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "file selected",Calendar.getInstance().getTimeInMillis(), 0);
        for (java.awt.event.ActionListener al : m_actionlisteners) 
            al.actionPerformed(null);
    }

    /**
     * Add a new action listener
     * @param al 
     */
    public void addActionListener(java.awt.event.ActionListener al) {
        this.m_actionlisteners.add(al);
    }

    /**
     * set the parsed file
     * if multiple files are permitted then the path is split by " | " and each part is interpreted as a file
     * @param path 
     */
    public void setFile(String path) {
        if (path.length() == 0)
            unsetFile();
        else {
            if (this.m_multipleFiles) {
                // test if this is an existing file or the parent folder exists
                if (new File(path).exists() || new File(path).getParentFile().exists()) {
                    // yes - assume it is a single file
                    setFile(new File(path));
                } else {
                    // no - assume it is mend as a list of files
                    String paths[] = path.split("\\s* | \\s*");
                    boolean allExists = true;
                    for (String p : paths) {
                        File f = new File(p);
                        allExists &= f.exists();
                    }
                    setFiles(paths);
                }
            } else {
                setFile(new File(path));
            }
            doActionPerformed();
        }
        
        
    }

    /**
     * set the parsed file
     * @param path 
     */
    public void setFile(File path) {
        if (path  == null) {
            m_file = null;
            return;
        }
        m_file = new File[]{path};
        txtFilePath.setText(m_file[0].getAbsolutePath());
        if ((path.exists()) &&  path.getAbsoluteFile().isDirectory())
            LocalProperties.setFolder(m_LocalPropertyKey, path.getAbsoluteFile());
        else
            LocalProperties.setFolder(m_LocalPropertyKey, path.getAbsoluteFile().getParent());

        doActionPerformed();
    }

    
    /**
     * set the parsed files
     * @param paths list of files 
     */
    public void setFiles(String[] paths) {
        File[] f = new File[paths.length];
        for (int i=0; i<paths.length;i++) {
            f[i]=new File(paths[i]);
        }
        setFiles(f);
    }
    
    /**
     * set the parsed files
     * @param paths list of files 
     */
    public void setFiles(File[] paths) {
        m_file = paths;
        if (paths  == null) {
            return;
        }
        txtFilePath.setText(RArrayUtils.toString(paths, " | "));
        
        if (paths.length>0 && (paths[0].exists()) &&  paths[0].isDirectory())
            LocalProperties.setFolder(m_LocalPropertyKey, paths[0]);
        else
            LocalProperties.setFolder(m_LocalPropertyKey, paths[0].getParent());

        doActionPerformed();
    }

    
    /**
     * set the selected file to null
     */
    public void unsetFile() {
        m_file = null;
    }

    /**
     * if the property for storing the last folder is the default key then try 
     * to make one that is specific for the current use of that field
     */
    protected void detectFrames() {
        if (m_LocalPropertyKey == DefaultLocalPropertyKey) {
            Component p = this;
            String pathKey;
            while (p.getParent() != null) {
                p = p.getParent();
                m_LocalPropertyKey += "." + p.getClass().getName();
            }
            if (p instanceof JFrame) {
                pathKey = ((JFrame) p).getTitle();
                m_LocalPropertyKey += "." + pathKey.replaceAll(" ", "");
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSelect = new javax.swing.JButton();
        txtFilePath = new javax.swing.JTextField();

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        setLayout(new java.awt.BorderLayout());

        btnSelect.setText("...");
        btnSelect.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        add(btnSelect, java.awt.BorderLayout.LINE_END);

        txtFilePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFilePathActionPerformed(evt);
            }
        });
        txtFilePath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFilePathFocusLost(evt);
            }
        });
        add(txtFilePath, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        selectFiles();

    }//GEN-LAST:event_btnSelectActionPerformed

    /**
     * shows a file-selection dialog and transfers the selection into the text-field
     */
    protected void selectFiles() {
        String file = null;
        String[] files = null;
        if (m_directoryOnly)
            file = GetFile.getFolder(LocalProperties.getFolder(m_LocalPropertyKey).getAbsolutePath(), this);
        else {
            if (m_doLoad) {
                files = GetFile.getFile(getExtensions(), getDescription(), LocalProperties.getFolder(m_LocalPropertyKey).getAbsolutePath(), getMultipleFiles(), this);
            } else {
                file = GetFile.saveFile( getExtensions(), getDescription(), LocalProperties.getFolder(m_LocalPropertyKey).getAbsolutePath(), this);
            }
        }
        
        if (file != null) {
            if (m_autoAddExtension && !file.contains(".") && getExtensions().length >0 && !new File(file).exists()) {
                if (m_autoAddDefaultExtension == null) {
                    for (String ext : getExtensions()) {
                        if (!ext.contentEquals("")) {
                            if (ext.startsWith("*"))
                                ext = ext.substring(1);
                            if (ext.startsWith("."))
                                ext = ext.substring(1);
                            file = file + "." + ext;
                            break;
                        }
                    }
                } else {
                    if (m_autoAddDefaultExtension.startsWith("."))
                        file = file + m_autoAddDefaultExtension;
                    else
                        file = file + "." + m_autoAddDefaultExtension;
                }
                
            }
            txtFilePath.setText(file);
            setFile(file);
        } else if (files!=null) {
            txtFilePath.setText(file);
            setFiles(files);
        }
    }

    private void txtFilePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFilePathActionPerformed
        setFile(txtFilePath.getText());
    }//GEN-LAST:event_txtFilePathActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        detectFrames();
    }//GEN-LAST:event_formAncestorAdded

    private void txtFilePathFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFilePathFocusLost
        if (txtFilePath.getText().length() == 0) 
            unsetFile();
        else
            setFile(txtFilePath.getText());
    }//GEN-LAST:event_txtFilePathFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelect;
    private javax.swing.JTextField txtFilePath;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns what is used to remember the last accessed folder
     * @return the m_LocalPropertyKey
     */
    public String getLocalPropertyKey() {
        return m_LocalPropertyKey;
    }

    /**
     * defines what is used to remember the last accessed folder
     * @param LocalPropertyKey the LocalPropertyKey to set
     */
    public void setLocalPropertyKey(String LocalPropertyKey) {
        this.m_LocalPropertyKey = LocalPropertyKey;
    }

    /**
     * files extensions to be shown as default
     * @return the extensions
     */
    public String[] getExtensions() {
        return m_extensions;
    }

    /**
     * files extensions to be shown as default
     * @param extensions the extensions to set
     */
    public void setExtensions(String[] extensions) {
        this.m_extensions = extensions;
    }

    /**
     * description shown for the default filtered file extensions
     * @return the description
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * description shown for the default filtered file extensions
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.m_description = description;
    }

    /**
     * the text in the file-browser - without trying to interpret it as a file
     * @return 
     */
    public String getText() {
        return txtFilePath.getText();
    }

    /**
     * sets the text in the file-browser - without trying to interpret it as a file
     * @return 
     */
    public void setText(String filePath) {
        txtFilePath.setText(filePath);
    }

    /**
     * the text for the file selection button
     * @return 
     */
    public String getButtonText() {
        return btnSelect.getText();
    }

    /**
     * the text for the file selection button
     * @param text 
     */
    public void setButtonText(String text) {
        btnSelect.setText(text);
    }

    /**
     * show "load" in the file selection dialog
     * @param load 
     */
    public void setLoad() {
        m_doLoad = true;
    }

    /**
     * define whether to show "load" or "save" in the file selection dialog
     * @param load 
     */
    public void setLoad(boolean showLoad) {
        m_doLoad = showLoad;
    }
    
    /**
     * is "Load" displayed in the file selection dialog
     * @return 
     */
    public boolean getLoad() {
        return m_doLoad;
    }
    
    /**
     * show "save" in the file selection dialog
     * @param load 
     */
    public void setSave() {
        m_doLoad = false;
    }

    /**
     * return the first selected file
     * @return 
     */
    public File getFile() {
        if (m_file == null || m_file.length == 0)
            return null;
        return m_file[0];
    }

    /**
     * return all selected files
     * @return 
     */
    public File[] getFiles() {
        return m_file;
    }    
    /**
     * should only directories be selectable
     * @return the m_directoryOnly
     */
    public boolean getDirectoryOnly() {
        return m_directoryOnly;
    }

    /**
     * should only directories be selectable
     * @param m_directoryOnly the m_directoryOnly to set
     */
    public void setDirectoryOnly(boolean m_directoryOnly) {
        this.m_directoryOnly = m_directoryOnly;
    }

    /**
     * whether extensions should automatically be added to the file names
     * @return the m_autoAddExtension
     */
    public boolean isAutoAddExtension() {
        return m_autoAddExtension;
    }

    /**
     * whether extensions should automatically be added to the file names
     * @param m_autoAddExtension the m_autoAddExtension to set
     */
    public void setAutoAddExtension(boolean m_autoAddExtension) {
        this.m_autoAddExtension = m_autoAddExtension;
    }

    /**
     * the extension that should automatically be added to the file names
     * if null and autoaddextension = true then the first of the filtered extensions is used
     * @return the m_autoAddExtension
     */
    public String isAutoAddDefaultExtension() {
        return m_autoAddDefaultExtension;
    }

    /**
     * the extension that should automatically be added to the file names
     * if null and autoaddextension = true then the first of the filtered extensions is used
     * @param m_autoAddExtension the m_autoAddExtension to set
     */
    public void setAutoAddDefaultExtension(String extension) {
        this.m_autoAddDefaultExtension = extension;
        if (extension != null) {
            setAutoAddExtension(true);
        }
    }

    /**
     * are multiple files selectable
     * @return the m_multipleFiles
     */
    public boolean getMultipleFiles() {
        return m_multipleFiles;
    }

    /**
     * are multiple files selectable
     * @param m_multipleFiles the m_multipleFiles to set
     */
    public void setMultipleFiles(boolean m_multipleFiles) {
        this.m_multipleFiles = m_multipleFiles;
    }

}
