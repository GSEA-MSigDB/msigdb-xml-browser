/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import edu.mit.broad.msigdb_browser.genome.swing.fields.FieldFactory;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

/**
 * Where to place the tabs
 */
public class TabPlacementPreference extends IntPreference {

    /**
     * @param name
     * @param desc
     * @param def
     */
    protected TabPlacementPreference(String name, String desc, int def) {
        super(name, desc, def);
    }

    public GFieldPlusChooser getSelectionComponent() {
        if (fField == null) {
            fField = FieldFactory.createTabPlacementField(getInt());
        }

        fField.setValue(getValue());
        return fField;
    }

} // End TabPlacementPreference

