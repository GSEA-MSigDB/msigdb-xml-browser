/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.vdb.chip.Chip;
import edu.mit.broad.msigdb_browser.vdb.chip.ChipHelper;

/**
 * choose 1 or more string
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 * @note diff from StringsInputParam -> the chpices are presented in a JListWindow and not
 * in a text area
 */
public class ChipChooserMultiParam extends WChipChooserAbstractParam implements IChipParam {

    /**
     * @param name
     * @param desc
     * @param reqd
     */
    public ChipChooserMultiParam(String name, String nameEnglish, String desc, boolean reqd) {
        super(name, nameEnglish, desc, reqd, true);
    }

    public Chip[] getChips() throws Exception {
        return super._getChips();
    }

    public Chip getChipCombo() throws Exception {
        Chip[] chips = getChips();

        // not sure why the chips.lemtgh == 0 check is needed - should this be generically caught?
        // see Probes2Symbol for an example of how this method fails (on no chip specified) if not checked in this manner
        if (isReqd() && chips.length == 0) {
            throw new IllegalArgumentException("Chips must be specified");
        }

        return ChipHelper.createComboChip(chips);
    }

}    // End class ChipsChooserParam

