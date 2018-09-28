/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import edu.mit.broad.msigdb_browser.xbench.core.WrappedComponent;

import javax.swing.*;

import java.awt.*;

/**
 * Class that defines windowing API's.
 * Most will trow a headless exception.
 */
public interface WindowManager {

    public JFrame getRootFrame() throws HeadlessException;

    public edu.mit.broad.msigdb_browser.xbench.core.Window openWindow(final WrappedComponent wc) throws HeadlessException;

    public edu.mit.broad.msigdb_browser.xbench.core.Window openWindow(final WrappedComponent wc, final Dimension dim) throws HeadlessException;

    public void showError(final Throwable t) throws HeadlessException;

    public void showError(final String msg, final Throwable t) throws HeadlessException;

    public boolean showConfirm(final String msg) throws HeadlessException;

    public void showMessage(final String msg) throws HeadlessException;

    public void showMessage(final String msg, final Component comp, final JButton[] customButtons, final boolean addCloseButton);

    public void showMessage(final String title, final String msg) throws HeadlessException;

    public DialogDescriptor createDialogDescriptor(final String title, final Component comp, final Action helpAction_opt);

    public DialogDescriptor createDialogDescriptor(final String title, final Component comp);

    // -------------------------------------------------------------------------------------------- //
    // ----------------------------------ACTION RELATED------------------------------------ //
    // -------------------------------------------------------------------------------------------- //

    public void runModalTool(final VTool tool, final DialogType dt);

} // End interface WindowManager
