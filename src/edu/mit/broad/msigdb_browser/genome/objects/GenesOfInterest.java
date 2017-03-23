/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.objects;

/**
 * Database of genes of interest
 * <p/>
 * Projects can maintain their own little db of why genes are relevant
 */
// TODO: Probably can refactor away the entire class.
public interface GenesOfInterest extends PersistentObject {

    public GeneSet getAsOneGeneSet();

} // End interface GenesofInterest
