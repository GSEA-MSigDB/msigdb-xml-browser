/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.actions.ext;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.xbench.actions.ExtAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;

import javax.swing.Icon;

import java.awt.event.ActionEvent;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;

/**
 * Action to launch a web browser
 *
 * @author Aravind Subramanian
 */
public class BrowserAction extends ExtAction {

    private static final String NAME = "Web Browser";
    private static final Icon ICON = JarResources.getIcon("Htm.gif");

    /**
     * Class Constructor.
     *
     * @param path
     */
    public BrowserAction(final String path) {
        this(NAME, null, ICON, path);
    }

    /**
     * Class constructor
     *
     * @param name
     * @param icon
     * @param path
     */
    public BrowserAction(final String name, final String desc, final Icon icon, final String path) {
        super("BrowserAction", name, desc, icon);
        setPath(path);
    }

    /**
     * Class constructor
     *
     * @param name
     * @param icon
     * @param file
     */
    public BrowserAction(final String name, final String desc, final Icon icon, final File file) {
        super("BrowserAction", name, desc, icon);
        setPath(file.toString());
    }

    /**
     * Action method.  Will launch browser and go to URI described in getPath.
     * Note that doesn't work if path is file path.  This method is overridden in
     * ShowAppRuntimeHomeDirAction and ShowDefaultOutputDirAction.
     * @param evt
     */
    public void actionPerformed(ActionEvent evt) {
        try {

            if (getPath() == null)
                throw new NullPointerException("null path associated with BrowserAction");

            Desktop.getDesktop().browse(new URI(getPath()));
            Application.getFileManager().registerRecentlyOpenedURL(getPath());

        } catch (Exception e) {
            Application.getWindowManager().showError("Could not launch browser", e);
        }
    }
}    // End BrowserAction
