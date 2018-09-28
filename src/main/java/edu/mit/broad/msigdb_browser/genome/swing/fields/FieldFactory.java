/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.swing.fields;

import edu.mit.broad.msigdb_browser.genome.utils.NamedInteger;

import javax.swing.*;

/**
 * Class FieldFactory
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
final public class FieldFactory {

    /**
     * Privatized class constructor
     */
    private FieldFactory() {
    }


    public static GComboBoxField createTabPlacementField(int def) {


        NamedInteger[] options = new NamedInteger[]{
                new NamedInteger(JTabbedPane.TOP, "Top"),
                new NamedInteger(JTabbedPane.BOTTOM, "Bottom"),
                new NamedInteger(JTabbedPane.LEFT, "Left"),
                new NamedInteger(JTabbedPane.RIGHT, "Right")
        };
        JComboBox cb = new JComboBox(new DefaultComboBoxModel(options));

        for (int i = 0; i < options.length; i++) {
            if (options[i].getValue() == def) {
                cb.setSelectedIndex(i);
                break;
            }
        }

        return new GComboBoxField(cb);
    }
}    // End FieldFactory
