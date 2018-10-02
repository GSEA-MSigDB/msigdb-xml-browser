/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.reports.api.ToolReport;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogType;
import edu.mit.broad.msigdb_browser.xbench.core.api.VTool;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Aravind Subramanian
 */
public abstract class AbstractVTool extends AbstractAction implements VTool {

    private DialogType fDialogType;

    private static final transient Logger klog = Logger.getLogger(AbstractVTool.class);

    // @todo assess if static is ok?
    protected static ToolReport kReport;

    private String fName;

    /**
     * Class constructor
     *
     * @param name
     * @param icon
     * @param dt
     */
    public AbstractVTool(final String name, final Icon icon, final DialogType dt) {
        super(name, icon);
        this.fDialogType = dt;
        this.fName = name;

        if (kReport == null) {
            klog.debug("Making vtoolreport");
            final File dir = Application.getVdbManager().getDefaultOutputDir();
            kReport = new ToolReport(dir);
        }

    }

    public String getName() {
        return fName;
    }

    public void actionPerformed(final ActionEvent evt) {
        Application.getWindowManager().runModalTool(this, fDialogType);
    }

    public String getHelpURL() {
        //return JarResources.getHelpURL(getName());
        return JarResources.getHelpURL(this.getClass().getName());
    }

}