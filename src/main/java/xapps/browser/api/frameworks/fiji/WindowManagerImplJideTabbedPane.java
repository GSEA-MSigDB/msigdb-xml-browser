/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.frameworks.fiji;

import com.jidesoft.swing.JideTabbedPane;

import edu.mit.broad.msigdb_browser.xbench.core.WrappedComponent;
import edu.mit.broad.msigdb_browser.xbench.core.api.AbstractWindowManager;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogType;
import edu.mit.broad.msigdb_browser.xbench.core.api.VTool;
import xapps.browser.api.vtools.VToolRunner;

import javax.swing.*;

import java.awt.*;

/**
 * @author Aravind Subramanian
 */
public class WindowManagerImplJideTabbedPane extends AbstractWindowManager {

    private VToolRunner fRunner;

    private JideTabbedPane fTabbedPane;

    /**
     * Class constructor
     *
     * @param frame
     * @param tp
     */
    public WindowManagerImplJideTabbedPane(final JFrame frame) {
        super(frame);

        fTabbedPane = new JideTabbedPane(JideTabbedPane.TOP);
        fTabbedPane.setTabEditingAllowed(false);
        //fTabbedPane.setBoxStyleTab(true);
        fTabbedPane.setShowCloseButton(true);
        fTabbedPane.setShowCloseButtonOnTab(true);
        fTabbedPane.setShowGripper(true); // just a decorator no action
        fTabbedPane.setShowIconsOnTab(true);
        fTabbedPane.setShowTabButtons(true);


        fTabbedPane.setFont(FONT_DEFAULT_PLAIN);
        fTabbedPane.setSelectedTabFont(FONT_DEFAULT_BOLD);

    }

    public static final Font FONT_DEFAULT_PLAIN = new Font("Helvetica",
            Font.PLAIN, 14);
    public static final Font FONT_DEFAULT_BOLD = new Font("Helvetica",
            Font.BOLD, 14);

    private int getTabIndex(WrappedComponent wc) {
        for (int i = 0; i < fTabbedPane.getTabCount(); i++) {
            Component comp = fTabbedPane.getComponentAt(i);
            if (comp == wc.getWrappedComponent()) {
                return i;
            }
        }

        return -1;
    }

    public edu.mit.broad.msigdb_browser.xbench.core.Window openWindow(final WrappedComponent wc) {

        int selIndex = getTabIndex(wc);
        if (selIndex != -1) { // helps the jumpiness a lot
            fTabbedPane.setSelectedIndex(selIndex);
        } else {
            fTabbedPane.addTab(wc.getAssociatedTitle(), wc.getAssociatedIcon(), wc.getWrappedComponent());
            fTabbedPane.setSelectedComponent(wc.getWrappedComponent());
        }

        return new JideWindow(wc.getAssociatedTitle(), wc.getWrappedComponent());
    }

    // -------------------------------------------------------------------------------------------- //
    // ---------------------------------- ACTION RELATED ------------------------------------ //
    // -------------------------------------------------------------------------------------------- //

    public void runModalTool(final VTool tool, final DialogType dt) {
        if (fRunner == null) {
            fRunner = new VToolRunner();
        }

        fRunner.showRunner(tool, dt);
    }

} // End class WindowManagerImpl
