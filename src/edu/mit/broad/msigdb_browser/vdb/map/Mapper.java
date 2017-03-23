/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;

import java.util.Set;

/**
 * @author Aravind Subramanian
 */
public interface Mapper {

    public MGeneSetMatrix map(final GeneSetMatrix sourceGm) throws Exception;

    public Set map(final String sourceProbeName) throws Exception;

}
