/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.reports.api;

import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;

import java.io.File;
import java.util.Properties;

/**
 * Read-Only interface for a Report eg reports already done
 * <p/>
 * Keep this here for junits clas slinker stuff
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */

public interface Report extends PersistentObject {

    public static final String PRODUCER_CLASS_ENTRY = "producer_class";

    public static final String TIMESTAMP_ENTRY = "producer_timestamp";

    public static final String FILE_ENTRY = "file";

    public static final String PARAM_ENTRY = "param";

    public File[] getFilesProduced();

    public Properties getParametersUsed();

    public Class getProducer();

    public long getTimestamp();

} // End Report

