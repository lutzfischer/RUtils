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

package org.rappsilber.gui;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.rappsilber.config.LocalProperties;

/**
 * a small wrapper for the JFileChooser
 * @author lfischer
 */
public class GetFile {
    /**
     * inner class that provides a simple filter for files, that are identified by the extension
     */
    private static class SimpleExtensionFilter extends FileFilter {
        /**
         * the file-extension (e.g. ".msm")
         */
        String[] m_extension;
        /**
         * description for the filter (e.g. "MSM-File (*.msm)")
         */
        String m_description;

        public SimpleExtensionFilter(String Extension,String Description) {
            m_extension = new String[]{Extension};
            m_description = Description;
        }

        public SimpleExtensionFilter(String[] Extension,String Description) {
            m_extension = Extension;
            m_description = Description;
        }

        @Override
        public boolean accept(File f) {
            
            if (f.isDirectory()) return true;
            if (m_extension == null)
                return true;

            for (String ext : m_extension)
                if  (f.getName().toLowerCase().endsWith(ext.toLowerCase()))
                    return true;
            return false;
        }

        @Override
        public String getDescription() {
            return m_description;
        }
    }

    public static String getFile(String[] FileExtension, String Description, String StartPath) {
        return getFile(FileExtension, Description, StartPath, null);
    }

    public static String getFile(String[] FileExtension, String Description, String StartPath, Component parent) {
        String[] ret = getFile(FileExtension, Description, StartPath, false, parent);
        if (ret == null ||ret.length == 0)
            return null;
        return ret[0];
    }

    public static String[] getFile(String[] FileExtension, String Description, String StartPath, boolean multiple) {
        return getFile(FileExtension, Description, StartPath, multiple, null);
    }

    public static String[] getFile(String[] FileExtension, String Description, String StartPath, boolean multiple, Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(StartPath));
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileFilter(new SimpleExtensionFilter(FileExtension, Description));
        jfc.setMultiSelectionEnabled(multiple);
        //jfc.showOpenDialog(null);
        int ret = jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            LocalProperties.setFolder(StartPath,jfc.getSelectedFile().getParentFile());
            File[] files = null;
            if (multiple) {
                files = jfc.getSelectedFiles();
            } else {
                files = new File[]{jfc.getSelectedFile()};
            }
            String[] s = new String[files.length];
            for (int i =0; i<s.length; i++) {
                s[i]=files[i].getAbsolutePath();
            }
            return s;
        } else
            return null;
    }

    public static String getFolder(String StartPath) {
        return getFolder(StartPath, null);
    }
    
    public static String getFolder(String StartPath, Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(StartPath));
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        jfc.setAcceptAllFileFilterUsed(true);
//        jfc.setAcceptAllFileFilterUsed(true);
        //jfc.setFileFilter();
        //jfc.showOpenDialog(null);
        int ret = jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            LocalProperties.setFolder(StartPath,jfc.getSelectedFile().getParentFile());
            return jfc.getSelectedFile().getAbsolutePath();
        } else
            return null;
    }
    
    public static File[] getFiles(String[] FileExtension, String Description, String StartPath) {
        return getFiles(FileExtension, Description, StartPath, null);
    }
    
    public static File[] getFiles(String[] FileExtension, String Description, String StartPath, Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(StartPath));
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setMultiSelectionEnabled(true);
        jfc.setFileFilter(new SimpleExtensionFilter(FileExtension, Description));
        //jfc.showOpenDialog(null);
        int ret = jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            LocalProperties.setFolder(StartPath,jfc.getSelectedFile().getParentFile());
            return jfc.getSelectedFiles();
        } else
            return null;
    }

    

    public static String getFile(String FileExtension, String Description, String StartPath) {
        return getFile(new String[]{FileExtension}, Description, StartPath);
    }

    public static String getFile(String FileExtension, String Description, String StartPath, Component parent) {
        return getFile(new String[]{FileExtension}, Description, StartPath, parent);
    }
    
    public static String getFile(String FileExtension, String StartPath) {
        return getFile(new String[]{FileExtension}, "*" + FileExtension, StartPath);
    }

    public static String getFile(String FileExtension, String StartPath, Component parent) {
        return getFile(new String[]{FileExtension}, "*" + FileExtension, StartPath, parent);
    }
    
    public static String getFile(String FileExtension[], String StartPath) {
        return getFile(FileExtension, "*" + FileExtension, StartPath);
    }

    public static String getFile(String FileExtension[], String StartPath, Component parent) {
        return getFile(FileExtension, "*" + FileExtension, StartPath, parent);
    }


    public static String saveFile(String[] FileExtension, String Description, String StartPath) {
        return saveFile(FileExtension, Description, StartPath, null);
    }
    public static String saveFile(String[] FileExtension, String Description, String StartPath, Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(StartPath));
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileFilter(new SimpleExtensionFilter(FileExtension, Description));
        int ret = jfc.showSaveDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath();
        } else
            return null;
    }

    public static String saveFile(String FileExtension, String Description, String StartPath) {
        return saveFile(new String[]{FileExtension}, Description, StartPath);
    }

    public static String saveFile(String FileExtension, String Description, String StartPath, Component parent) {
        return saveFile(new String[]{FileExtension}, Description, StartPath, parent);
    }

    public static String saveFile(String FileExtension, String StartPath) {
        return saveFile(new String[]{FileExtension}, "*" + FileExtension, StartPath);
    }

    public static String saveFile(String FileExtension[], String StartPath) {
        return saveFile(FileExtension, "*" + FileExtension, StartPath);
    }


}
