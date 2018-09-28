/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import com.jidesoft.dialog.BannerPanel;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;

/**
 * @author Aravind Subramanian
 */
public interface DialogType {

    public static final DialogType ParamChooserPanel = new ParamChooserPanel();

    public JComponent createBannerPanel(String title);

    static abstract class AbstractDialogType implements DialogType {

        private String fName;

        /**
         * Class constructor
         *
         * @param name
         */
        AbstractDialogType(String name) {
            this.fName = name;
        }

        public String toString() {
            return fName;
        }

        public int hashCode() {
            return fName.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj != null && obj instanceof DialogType) {
                return obj.toString().equals(fName);
            }

            return false;
        }

    }

    class ParamChooserPanel extends AbstractDialogType {

        ParamChooserPanel() {
            super("pcp");
        }

        public JComponent createBannerPanel(String title) {
            String desc = "Please enter all required (red) parameters below";

            BannerPanel headerPanel1 = new BannerPanel(title,
                    desc,
                    JarResources.getImageIcon("param_chooser_dialog_big.gif"));

            headerPanel1.setFont(new Font("Tahoma", Font.PLAIN, 11));
            headerPanel1.setBackground(Color.WHITE);
            headerPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            return headerPanel1;
        }

    }

} // End class DialogType

