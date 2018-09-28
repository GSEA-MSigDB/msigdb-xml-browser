/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import edu.mit.broad.msigdb_browser.genome.swing.fields.GDirFieldPlusChooser;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

import java.io.File;

/**
 * @author Aravind Subramanian
 */
public class DirPreference extends AbstractPreference {

    private GDirFieldPlusChooser fChooser;

    /**
     * @param name
     * @param desc
     * @param def
     */
    protected DirPreference(String name, String desc, File def) {
        super(name, desc, def);
    }

    public Object getValue() {
        String s = kPrefs.get(getName(), ((File) getDefault()).getPath());
        return new File(s);
    }

    public File getDir(boolean makeItIfItDoesntAlreadyExist) {
        //klog.debug("$$$ " + getValue());
        File f = (File) getValue();
        if ((f.exists() == false) && (makeItIfItDoesntAlreadyExist)) {
            boolean success = f.mkdir();
            klog.info("Made pref dir: " + f + " status: " + success);
        }
        return (File) getValue();
    }

    public void setValue(Object value) throws Exception {
        kPrefs.put(getName(), value.toString());
    }

    public GFieldPlusChooser getSelectionComponent() {

        if (fChooser == null) {
            fChooser = new GDirFieldPlusChooser();
            fChooser.setValue(getValue());
        }

        fChooser.setValue(getValue());

        //klog.debug("Getting component");

        return fChooser;

    }

} // End DirPreference