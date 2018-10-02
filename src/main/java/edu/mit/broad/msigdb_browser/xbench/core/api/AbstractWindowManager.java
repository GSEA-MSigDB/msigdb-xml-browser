/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import edu.mit.broad.msigdb_browser.xbench.core.ApplicationDialog;
import edu.mit.broad.msigdb_browser.xbench.core.WrappedComponent;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.*;

/**
 * @author Aravind Subramanian
 */
public abstract class AbstractWindowManager implements WindowManager {

    private JFrame fRootFrame;

    protected static final Logger klog = Logger.getLogger(AbstractWindowManager.class);

    /**
     * Class constructor
     *
     * @param rootFrame
     */
    public AbstractWindowManager(final JFrame rootFrame) {
        if (rootFrame == null) {
            throw new IllegalArgumentException("Param rootFrame cannot be null");
        }
        this.fRootFrame = rootFrame;

    }

    // @todo dim
    public edu.mit.broad.msigdb_browser.xbench.core.Window openWindow(final WrappedComponent wc, final Dimension dim) {
        return openWindow(wc);
    }

    public JFrame getRootFrame() {
        //klog.debug("Getting root frame: " + fRootFrame + " " + fRootFrame.getName());
        return fRootFrame;
    }

    // -------------------------------------------------------------------------------------------- //
    // ----------------------------- DISPLAYS A MESSAGE INFO, WARNING ETC ------------------------- //
    // -------------------------------------------------------------------------------------------- //

    public void showError(final String msg, final Throwable t) {
        klog.error(msg, t);
        ApplicationDialog.showError(msg, t);
    }

    public void showError(final Throwable t) {
        showError("Error", t);
    }

    public boolean showConfirm(final String msg) {
        klog.info(msg);
        return ApplicationDialog.showConfirm("Please confirm this action", msg);
    }

    public void showMessage(final String msg) {
        klog.info(msg);
        ApplicationDialog.showMessage(msg);
    }

    public void showMessage(final String msg, final Component comp, final JButton[] customButtons, final boolean addCloseButton) {
        klog.info(msg);
        ApplicationDialog.showMessage(msg, comp, customButtons, addCloseButton);
    }

    public void showMessage(final String title, final String msg) {
        klog.info(msg);
        ApplicationDialog.showMessage(title, msg);
    }

    public DialogDescriptor createDialogDescriptor(final String title, final Component comp, final Action help_action_opt) {
        return new DialogDescriptorJide(title, comp, help_action_opt);
    }

    public DialogDescriptor createDialogDescriptor(final String title, final Component comp) {
        //return new DialogDescriptor_v1(title, comp, null);
        return new DialogDescriptorJide(title, comp, null);
    }

} // End class WindowManagerImpl
