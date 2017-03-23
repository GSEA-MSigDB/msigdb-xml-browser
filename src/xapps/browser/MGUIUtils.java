/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser;

import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.dialog.ButtonPanel;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import javax.swing.*;

/**
 * @author Aravind Subramanian
 */
public class MGUIUtils {

    public static JPanel createExportSetsControlPanel(VToolsForMSigDBBrowser.ViewerHook hook_only_sel,
                                                      VToolsForMSigDBBrowser.ViewerHook hook_all) {

        ButtonPanel bp = new ButtonPanel();

        bp.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));

        JButton bHelp = JarResources.createHelpButton("msigdb_browser");
        bp.addButton(bHelp, ButtonNames.HELP);

        JButton bExportAll = new JButton("Export sets as GeneSetMatrix", JarResources.getIcon("ExportAsGeneSetMatrix.gif"));
        bExportAll.addActionListener(new VToolsForMSigDBBrowser.ExportGeneSets(hook_all));
        bp.addButton(bExportAll);

        return bp;
    }
} // End class MGUIUtils
