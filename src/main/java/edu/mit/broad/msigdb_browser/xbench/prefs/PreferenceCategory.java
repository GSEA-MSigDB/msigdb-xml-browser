/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

/**
 * @author Aravind Subramanian
 */
//TODO: suspect this is really unused.
class PreferenceCategory {

    private Preference[] fPrefs;

    PreferenceCategory(Preference[] prefs) {
        this.fPrefs = new Preference[prefs.length]; // shallow clone
        for (int i = 0; i < prefs.length; i++) {
            this.fPrefs[i] = prefs[i];
        }
    }

}
