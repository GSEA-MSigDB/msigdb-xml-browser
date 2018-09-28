/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Base action-pattern class for all xomics actions.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public abstract class XAction extends AbstractAction {
    public static final String ID = "ID";

    public XAction(String id, String name, String description) {
        this(id, name, description, null);
    }

    public XAction(String id, String name, String description, Icon icon) {
        super();
        super.putValue(ID, id);
        super.putValue(NAME, name);
        if (description != null) super.putValue(SHORT_DESCRIPTION, description);
        if (icon != null) super.putValue(SMALL_ICON, icon);
    }
}    // End XAction
