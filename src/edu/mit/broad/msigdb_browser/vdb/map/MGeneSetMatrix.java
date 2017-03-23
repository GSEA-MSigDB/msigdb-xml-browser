/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;

/**
 * @author Aravind Subramanian
 */
public interface MGeneSetMatrix {

    public GeneSetMatrix getMappedGeneSetMatrix(final String prefix);

} // End interface MGeneSetMatrix
