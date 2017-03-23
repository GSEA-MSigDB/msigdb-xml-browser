/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.actions;

import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;

/**
 * @author Aravind Subramanian
 */
public class ShowDefaultOutputDirAction extends BrowserAction {

    /**
     * Class constructor
     *
     * @param name
     */
    public ShowDefaultOutputDirAction(String name) {
        super(name, "Show output directory of this application",
                null, Application.getVdbManager().getDefaultOutputDir());
    }


    public void actionPerformed(ActionEvent evt) {
        super.setPath(Application.getVdbManager().getDefaultOutputDir().getPath()); // might have changed since init
        try {

            if (getPath() == null)
                throw new NullPointerException("null path associated with BrowserAction");

            URI outputDirURI = new File(getPath()).toURI();
            // we need to add an (empty) authority designator for compatibility with all platforms
            // (mac requires an authority field in file URIs, windows does not)
            // the resulting URI will have the form "file://<absolute path>"
            outputDirURI = new URI(outputDirURI.getScheme(), "", outputDirURI.getPath(), null, null);
            Desktop.getDesktop().browse(outputDirURI);
            Application.getFileManager().registerRecentlyOpenedURL(getPath());
        } catch (Exception e) {
            Application.getWindowManager().showError("Could not launch browser", e);
        }
    }

} // End class ShowAppOutputDirAction
