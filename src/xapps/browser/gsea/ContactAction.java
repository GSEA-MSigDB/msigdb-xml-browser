/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;

public class ContactAction extends BrowserAction {

    public ContactAction() {
        super("Contact Us", "Contact Us", null, GseaWebResources.getGseaContactURL());
    }

}