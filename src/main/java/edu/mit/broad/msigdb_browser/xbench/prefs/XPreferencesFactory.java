/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import edu.mit.broad.msigdb_browser.genome.Conf;
import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;
import edu.mit.broad.msigdb_browser.genome.utils.SystemUtils;
import edu.mit.broad.msigdb_browser.xbench.actions.XAction;
import edu.mit.broad.msigdb_browser.xbench.core.ApplicationDialog;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * IMP IMP be very careful with using graphics stuff here -> need to always check for headless mode!!
 * <p/>
 * IMP IMP: Dont use RuntimeResources to avoid recursion!!
 */
public class XPreferencesFactory {

    private static final Logger klog = Logger.getLogger(XPreferencesFactory.class);

    /**
     * runtime home directory (i.e user's home for the application
     * Under ~home.
     */
    // IMP must be declared before the recent files/dirs are made (else they are made in pwd -- remember the jws desktop issue!)
    // IMP This is NOT a preference!
    public static File kAppRuntimeHomeDir;
    
    static {
        kAppRuntimeHomeDir = new File(SystemUtils.getUserHome(), "gsea_home");
        if (kAppRuntimeHomeDir.exists() == false) {
            boolean made = kAppRuntimeHomeDir.mkdir();
            if (!made) {
                klog.fatal("Could not make gsea_home dir at: >" + kAppRuntimeHomeDir + "<");
            }
        }

        klog.debug("kAppRuntimeHomeDir: " + kAppRuntimeHomeDir + " " + kAppRuntimeHomeDir.exists());
    }

    // @todo Maybe store as a pref later

    public static int getToolTreeWidth() {
        if (Conf.isGseaApp()) {
            return 300;
        } else {
            return 250;
        }
    }

    public static int getToolTreeWidth_min() {
        if (Conf.isGseaApp()) {
            return 150;
        } else {
            return 150;
        }
    }

    public static int getToolTreeDivLocation() {
        return 350;
    }

    /**
     * Privatized class constructor
     */
    private XPreferencesFactory() {
    }

    /*
     * GENERAL
     */
    // TODO: Confirm: don't think these are used.
    public static final BooleanPreference kAskBeforeAppShutdown = new BooleanPreference("Prompt before closing application",
            "Display a prompt asking for confirmation before shutting down the application",
            false);

    public static final BooleanPreference kOnlineMode = new BooleanPreference("Connect over the Internet",
            "You can connect to the GSEA website over the Internet. This ensures you always get the current version of gene sets and chip annotations. ",
            true);

    public static final StringPreference kLastToolName = new StringPreference("Last Tool Run",
            "Dont change me",
            "");
    
    public static final DirPreference kDefaultReportsOutputDir = new DirPreference("Browser reports output folder",
            "Location of the output directory where Browser tool reports are stored",
            new File(kAppRuntimeHomeDir, "output_browser"));

    /**
     * @param pref
     * @return
     */
    public static Object showSetPreferenceDialog(final Preference pref) {
        GFieldPlusChooser field = pref.getSelectionComponent();
        String title = "Set preference: " + pref.getName();

        JPanel input = new JPanel(new BorderLayout());

        JLabel label = new JLabel(pref.getName() + ": ");
        label.setFont(GuiHelper.FONT_DEFAULT_BOLD);
        input.add(label, BorderLayout.WEST);
        input.add(field.getComponent(), BorderLayout.CENTER);
        input.add(new JLabel(pref.getDesc()), BorderLayout.SOUTH);

        ApplicationDialog dd = new ApplicationDialog(title, input);
        int res = dd.show();
        if (res == ApplicationDialog.OK_OPTION) {
            try {
                pref.setValue(field.getValue());
            } catch (Throwable t) {
                Application.getWindowManager().showError("Could not set preference: " + pref.getName(), t);
                return null;
            }
        }

        return pref.getValue();
    }

    public static JButton createActionButton(Preference pref) {
        return new JButton(new GenericPrefAction(pref));
    }

    static class GenericPrefAction extends XAction {

        Preference fPref;

        /**
         * Class Constructor.
         */
        GenericPrefAction(Preference pref) {
            this(pref, null);
        }

        GenericPrefAction(Preference pref, Icon customIcon) {
            super("GenericPrefAction", pref.getName(), pref.getDesc(), customIcon);
            this.fPref = pref;
        }

        public void actionPerformed(ActionEvent evt) {
            showSetPreferenceDialog(fPref); // ignore the return value
        }
    }    // End GenericPrefAction

} // End XPreferencesFactory
