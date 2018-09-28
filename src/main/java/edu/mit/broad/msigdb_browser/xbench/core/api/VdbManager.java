/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import java.io.File;

/**
 * Class that defines databases that are widely available
 */
public interface VdbManager {

    public File getRuntimeHomeDir();

    public File getDatabasesDir();

    public File getDefaultOutputDir();

} // End interface VdbManager
