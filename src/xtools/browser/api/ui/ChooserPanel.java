/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.ui;

import javax.swing.*;

/**
 * @author Aravind Subramanian
 */
public interface ChooserPanel {

    public JComponent getChooser();

    public String getTitle();

    public Object[] getChoosenObjects();

    public JList[] getJListsForDoubleClick();

} // End interface ChooserPanel
