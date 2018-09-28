/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

import java.util.prefs.Preferences;

/**
 * @author Aravind Subramanian
 */
public interface Preference {

    // TODO: adjust this so that it doesn't affect the Desktop settings.
    public static String PATHNAME = Preference.class.toString();

    public static Preferences kPrefs = Preferences.userRoot().node(PATHNAME);

    public String getName();

    public String getDesc();

    public Object getValue();

    public void setValue(final Object newValue) throws Exception;

    public GFieldPlusChooser getSelectionComponent();

}


