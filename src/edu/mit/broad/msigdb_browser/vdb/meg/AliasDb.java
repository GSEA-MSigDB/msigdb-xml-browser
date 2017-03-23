/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.meg;

import edu.mit.broad.msigdb_browser.vdb.chip.Probe;

/**
 * @author Aravind Subramanian
 */
public interface AliasDb {

    public boolean isAlias(final String alias) throws Exception;

    public Gene getHugo(final String alias) throws Exception;

    public Probe[] getAliasesAsProbes() throws Exception;

} // End class IAliasDb
