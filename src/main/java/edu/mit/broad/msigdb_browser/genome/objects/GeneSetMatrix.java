/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.objects;

import java.util.List;

/**
 * Essentially a collection of (probably related) GeneSets
 * In additin has colors and icons and names
 * <p/>
 * Lightweigth container for a bunch of genesets -- geneset data is not duplicated
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public interface GeneSetMatrix extends PersistentObject {

    public GeneSetMatrix cloneShallow(String newName);

    public int getNumGeneSets();

    /**
     * return a ref to the real GeneSet -> @todo consider cloning?
     *
     * @param i
     * @return
     */
    public GeneSet getGeneSet(final int i);


    public GeneSet getGeneSet(final String gsetName);

    /**
     * All genesets
     * directly -- no cloning
     *
     * @return
     */
    public GeneSet[] getGeneSets();

    public List getGeneSetsL();

    /**
     * The number of members in the biggest GeneSet.
     *
     * @return
     */
    public int getMaxGeneSetSize();

    public GeneSet getAllMemberNames_gset();

} // End interface GeneSetMatrix
