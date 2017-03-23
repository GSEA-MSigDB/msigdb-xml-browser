/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.actions;

import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.prefs.XPreferencesFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.io.File;

/**
 * @author Aravind Subramanian
 */
public class ShowAppRuntimeHomeDirAction extends BrowserAction {

    /**
     * Class constructor
     *
     * @param name
     */
    public ShowAppRuntimeHomeDirAction(final String name) {
        super(name, "Show runtime home directory of this application", null, XPreferencesFactory.kAppRuntimeHomeDir);
    }

    public void actionPerformed(ActionEvent evt) {
        super.setPath(XPreferencesFactory.kAppRuntimeHomeDir.getPath()); // might have changed since init
        try {

            if (getPath() == null)
                throw new NullPointerException("null path associated with BrowserAction");

            URI homeDirURI = new File(getPath()).toURI();
            // we need to add an (empty) authority designator for compatibility with all platforms
            // (mac requires an authority field in file URIs, windows does not)
            // the resulting URI will have the form "file://<absolute path>"
            homeDirURI = new URI(homeDirURI.getScheme(), "", homeDirURI.getPath(), null, null);
            Desktop.getDesktop().browse(homeDirURI);
            Application.getFileManager().registerRecentlyOpenedURL(getPath());

        } catch (Exception e) {
            Application.getWindowManager().showError("Could not launch browser", e);
        }
    }

}    // End ShowAppRuntimeHomeDirAction