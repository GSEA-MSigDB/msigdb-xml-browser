/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import javax.swing.*;

/**
 * has some magic to make jlists double clickable
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public interface DialogDescriptor {

    /**
     * class constant for oks
     */
    public static final int OK_OPTION = 0;

    /**
     * class constant for cancel actions
     */
    public static final int CANCEL_OPTION = 2;

    public void setOnlyShowCloseOption();

    /**
     * If modal, a regular jdialog is always used
     * If not modal:
     * 1) if application (desktop) is available then the dialog is shown in a jif
     * (why? ->else the dial dissapears behind the desktop)
     * 2) If app is null, then the dialog iks shows in a jdialog
     * (same as modal, except jdialog is not modal)
     * <p/>
     * Known issue -> This does not work when launched from within JUNIT -> it always
     * makes a jdialog
     *
     * @return One of OK or CANCEL
     */
    public int show();


    /**
     * only makes sense if the specified jlist is a component that is
     * displayed in the dialog descriptor window
     * Double click / enter on the jlist == a OK button click
     * Simpley closes the dialog and returns void when double clicked (the OK is implied)
     */
    public void enableDoubleClickableJList(final JList jl);
}    // End DialogDescriptor
