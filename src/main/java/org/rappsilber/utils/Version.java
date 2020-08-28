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
package org.rappsilber.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lfischer
 */
public class Version {
    /** build - svn-revision*/
    public Integer build;
    /** extension */
    public String extension;
    /** the major number of the version*/
    public int major;
    /** the minor number of the version*/
    public int minor;

    public Version(int major, int minor, Integer build) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.extension="";
    }
    public Version(int major, int minor, Integer build, int extension) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.extension=""+extension;
    }

    public Version(int major, int minor, String svn_refbuild) {
        this.major = major;
        this.minor = minor;
        int tb;
        String extension = null;
        try {
            this.build = Integer.parseInt(svn_refbuild.replaceAll("\\$Rev:\\s*", "").replaceAll("\\s*\\$", ""));
        } catch (NumberFormatException pe) {
            this.extension = svn_refbuild;
        }
    }
    
    
    
    public Version(int major, int minor, int build, String svn_refbuild) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        setExtension(svn_refbuild);
    }

        
    public String setExtension(String svn_refbuild) {
        if (svn_refbuild.matches("\\$Rev:\\s*[0-9]+\\s*\\$"))
            this.extension = ""+Integer.parseInt(svn_refbuild.replaceAll("\\$Rev:\\s*", "").replaceAll("\\s*\\$", ""));
        else {
            this.extension = svn_refbuild;
        }
        return this.extension;
    }

    public String toLongString() {
        if (extension.isEmpty() || extension.contentEquals("0"))
            return String.format("%02d.%02d.%07d", major ,minor ,build);
        return String.format("%02d.%02d.%02d.%07d", major ,minor ,build, extension);
    }

    @Override
    public String toString() {
        
        return major + "." + minor +  (build == null?"": "." + build) + (extension.isEmpty() ? "":  "."+extension );
    }
    
    public static Version parseEmbededVersion(String propertyFile, String property) {
        
        final Properties properties = new Properties();
        try {
            properties.load(Version.class.getResourceAsStream(propertyFile));
        } catch (Exception e) {
            try {
                properties.load(Version.class.getClassLoader().getResourceAsStream(propertyFile));                
            }catch (Exception ex) {
                Logger.getLogger(Version.class.getName()).log(Level.WARNING,"Could not parse version will be set to 0.0.0",ex);
            }
        }
        String[] v = properties.getProperty(property).split("\\.",4);
        
        int major = 0;
        int minor = 0;
        Integer build = null;
        String extension = null;
        try {major = Integer.parseInt(v[0]);} catch (Exception e) {Logger.getLogger(Version.class.getName()).log(Level.WARNING,"Error parsing version major:", e);}
        try {minor = Integer.parseInt(v[1]);} catch (Exception e) {Logger.getLogger(Version.class.getName()).log(Level.WARNING,"Error parsing version minor:", e);}
        if (v.length>2) {
            try {
                build = Integer.parseInt(v[2]);
            } catch (Exception e) {
                if (v.length>3) {
                    Logger.getLogger(Version.class.getName()).log(Level.WARNING,"Error parsing version build:", e);
                } else {
                    extension = v[2];
                }
            }
        }
        
        Version version = new Version(major, minor, build);
        if (v.length > 3) {
            if (extension == null) {
                extension = v[3];
            } else {
                extension += "." + v[3];
            }
        }
        if (extension != null) {
            version.setExtension(extension);
        }
        return version;
    }
    
}
