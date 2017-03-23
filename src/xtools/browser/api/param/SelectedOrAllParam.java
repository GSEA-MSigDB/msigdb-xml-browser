/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

public class SelectedOrAllParam extends StringReqdParam {

    public static final String ONLY_SELECTED = "Only selected items";

    public static final String ALL = "All items";

    /**
     * Class constructor
     *
     * @param reqd
     */
    public SelectedOrAllParam() {
        super("mode", "Items to use for analysis", "Specify items to use for analysis", new String[]{ALL, ONLY_SELECTED});
    }

    public boolean isOnlySelected() {
        return getString().equals(ONLY_SELECTED);
    }

    public boolean isFileBased() {
        return false;
    }

} // End class SelectedOrAllParam
