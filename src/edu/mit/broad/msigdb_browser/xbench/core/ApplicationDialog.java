/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core;

import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogDescriptor;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogDescriptorJide;

import javax.swing.*;

import java.awt.*;

/**
 * Usually modal, application wide dialogs. They block GUI usage
 * until user chooses an option.
 * <p/>
 * NO internal dialogs stuff!!
 * <p/>
 * Auot sets the application as the parent component
 * <p/>
 * Uses JOptionPane internally, but adds some xomics candy.
 * plus has some magic to make jlists double clickable
 * <p/>
 * IMP: much of the code is borrowed from DialogDescriptor
 * <p/>
 * Advantage in parcelling this off into a class is that we redecue client codes
 * usage of Application.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 * @note Importance of setting the parent component
 * Having no parent component (or the JOptionPane.getRootFrame() one) causes
 * a modal issue wherein a GUI with the model window showing if minimized
 * appears to hang (the CTRL-DELETE-OPTION / GeneCluster bug that Keith noticed)
 * @see DialogDescriptor
 */
public class ApplicationDialog extends DialogDescriptorJide {

    /**
     * Class Constructor.
     *
     * @param comp
     * @param title
     */
    public ApplicationDialog(final String title, final Component comp) {
        super(title, comp, null);
    }

    private static void foo2(final String title,
                             final Throwable t_opt) {

        _show(new ErrorWidgetJide2(Application.getWindowManager().getRootFrame(), title, t_opt, null));
    }

    private static void _show(ErrorWidgetJide2 ew) {
        ew.pack();
        ew.setLocationRelativeTo(Application.getWindowManager().getRootFrame());
        ew.setVisible(true);
    }

    public static void showError(final String shortErrorDesc, final Throwable t) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                foo2(shortErrorDesc, t);
            }
        });
    }

    public static void showMessage(final String title, final Component comp, final JButton[] customButtons, final boolean addCloseButton) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApplicationDialog ad = new ApplicationDialog(title, comp);
                ad.setButtons(customButtons, addCloseButton);
                ad.getCancelButton().setText("Close");
                //ad.setOnlyShowCloseOption();
                ad.show();
            }
        });
    }

    public static void showMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(Application.getWindowManager().getRootFrame(), msg);
            }
        });
    }

    public static void showMessage(final String title, final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(Application.getWindowManager().getRootFrame(), msg, title, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static boolean showConfirm(final String title, String msg) {
        int res = JOptionPane.showConfirmDialog(Application.getWindowManager().getRootFrame(), msg, title, JOptionPane.OK_CANCEL_OPTION);
        return res == ApplicationDialog.OK_OPTION;
    }

}    // End ApplicationDialog
