/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.chip;

import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

/**
 * @author Aravind Subramanian
 */
public interface NullSymbolMode {

    public String getSymbol(String probeId, Gene gene);

}
