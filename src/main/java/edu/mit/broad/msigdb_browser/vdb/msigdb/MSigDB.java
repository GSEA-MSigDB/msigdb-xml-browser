/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;
import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;

/**
 * @author Aravind Subramanian
 */
public interface MSigDB extends PersistentObject {

    public String getName();

    public String getVersion();

    public String getBuildDate();

    public int getNumGeneSets();

    public GeneSetAnnotation getGeneSetAnnotation(int g);

    public GeneSetAnnotation getGeneSetAnnotation(final String gsStdName);

    public GeneSetMatrix createGeneSetMatrix(final String[] gsetNames);

} // End interface MSigDb
