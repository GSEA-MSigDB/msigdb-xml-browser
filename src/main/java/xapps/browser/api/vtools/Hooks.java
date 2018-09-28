/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import javax.swing.*;

/**
 * These are like tools except they work in the user interface
 *
 * @author Aravind Subramanian
 */
public class Hooks {

    public static final Icon ICON_SAVE = JarResources.getIcon("Save16.gif");
    public static final Icon ICON_SAVE_GCT = JarResources.getIcon("Gct16.gif");

    public static interface BaseHook {

        // @note need to change this to save name
        public String getSaveNameHint();

        public String getSaveName();

        public String getComponentMessage();

        public JTextField getTextField();

    }

    public static interface Hook extends BaseHook {

    }

    public static abstract class AbstractHook implements Hook {

        private JTextField tfName;

        public JTextField getTextField() {

            if (tfName == null) {
                tfName = new JTextField(60);

                tfName.setBorder(BorderFactory.createTitledBorder("Enter a short name for the result"));
            }

            String sn = getSaveNameHint();
            if (sn != null && sn.length() > 0) {
                tfName.setText(sn);
            }

            return tfName;
        }

        public String getSaveName() { // dont make safe - give back whatever user asks for
            if (tfName != null) {
                return tfName.getText();
            } else {
                return "na";
            }
        }

    } // End class AbstractHook


} // End class Hooks

