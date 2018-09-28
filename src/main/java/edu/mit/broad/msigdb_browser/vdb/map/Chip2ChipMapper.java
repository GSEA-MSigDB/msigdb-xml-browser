/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;

/**
 * @author Aravind Subramanian
 */
public interface Chip2ChipMapper extends PersistentObject, Mapper {

    public Chip getTargetChip();

} // End interface Chip2ChipMapper
