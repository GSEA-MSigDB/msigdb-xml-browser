/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.ui;

import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogDescriptor;

import javax.swing.*;

/**
 * @author Aravind Subramanian
 */
public class ChooserWindow {

    private ChooserPanel fChooserPanel;

    /**
     * Class constructor
     *
     * @param cp
     */
    public ChooserWindow(final ChooserPanel cp) {

        if (cp == null) {
            throw new IllegalArgumentException("Param cp cannot be null");
        }

        this.fChooserPanel = cp;
    }

    public Object[] show() {

        DialogDescriptor desc = Application.getWindowManager().createDialogDescriptor(fChooserPanel.getTitle(), fChooserPanel.getChooser());

        JList[] jlists = fChooserPanel.getJListsForDoubleClick();
        if (jlists != null) {
            for (int i = 0; i < jlists.length; i++) {
                desc.enableDoubleClickableJList(jlists[i]);
            }
        }

        int res = desc.show();

        if (res == DialogDescriptor.CANCEL_OPTION) {
            return null;
        } else {
            return fChooserPanel.getChoosenObjects();
        }

    }

} // End class ChooserDisplay
