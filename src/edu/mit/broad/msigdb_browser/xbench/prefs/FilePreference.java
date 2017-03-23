/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import edu.mit.broad.msigdb_browser.genome.swing.choosers.GFileFieldPlusChooser;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

import java.io.File;

/**
 * @author Aravind Subramanian
 */
public class FilePreference extends AbstractPreference {

    private GFileFieldPlusChooser fChooser;

    /**
     * @param name
     * @param desc
     * @param def
     */
    protected FilePreference(final String name,
                             final String desc,
                             final File def) {
        super(name, desc, def);
    }

    public Object getValue() {
        String s = kPrefs.get(getName(), ((File) getDefault()).getPath());
        return new File(s);
    }

    public File getFile() {
        return (File) getValue();
    }

    public void setValue(Object value) throws Exception {
        kPrefs.put(getName(), value.toString());
    }

    public GFieldPlusChooser getSelectionComponent() {
        if (fChooser == null) {
            fChooser = new GFileFieldPlusChooser();
        }

        //klog.debug("Getting component");
        fChooser.setValue(getValue());
        return fChooser;
    }

} // End FilePreference