/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;

public class GseaHelpAction extends BrowserAction {

    public GseaHelpAction() {
        super("GSEA documentation", "Online documentation of the GSEA algorithm and software",
                GuiHelper.ICON_HELP16, GseaWebResources.getGseaHelpURL());
    }

    public GseaHelpAction(String name, String desc, String url) {
        super(name, desc, GuiHelper.ICON_HELP16, url);
    }

}    // End GseaHelpAction